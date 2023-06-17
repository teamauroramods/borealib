package com.teamaurora.borealib.impl.network.forge;

import com.google.common.base.Suppliers;
import com.teamaurora.borealib.api.network.v1.message.BorealibPacket;
import com.teamaurora.borealib.api.network.v1.message.PacketDecoder;
import com.teamaurora.borealib.impl.network.NetworkManagerImpl;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.function.Supplier;

@ApiStatus.Internal
public class NetworkChannelImpl {

    protected final SimpleChannel channel;
    protected final Supplier<Object> clientMessageHandler;
    protected final Supplier<Object> serverMessageHandler;
    protected int nextId;

    protected NetworkChannelImpl(SimpleChannel channel, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        this.channel = channel;
        this.clientMessageHandler = Suppliers.memoize(clientFactory::get);
        this.serverMessageHandler = Suppliers.memoize(serverFactory::get);
    }

    protected static NetworkDirection convert(@Nullable BorealibPacket.Direction direction) {
        if (direction == null)
            return null;
        return switch (direction) {
            case PLAY_SERVERBOUND -> NetworkDirection.PLAY_TO_SERVER;
            case PLAY_CLIENTBOUND -> NetworkDirection.PLAY_TO_CLIENT;
            case LOGIN_SERVERBOUND -> NetworkDirection.LOGIN_TO_SERVER;
            case LOGIN_CLIENTBOUND -> NetworkDirection.LOGIN_TO_CLIENT;
        };
    }

    protected <MSG extends BorealibPacket<T>, T> SimpleChannel.MessageBuilder<MSG> getMessageBuilder(Class<MSG> clazz, PacketDecoder<MSG, T> decoder, @Nullable BorealibPacket.Direction direction) {
        return this.channel.messageBuilder(clazz, this.nextId++, convert(direction)).encoder((msg, buf) -> {
            try {
                msg.write(buf);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to write packet data", e);
            }
        }).decoder(buf -> {
            try {
                return decoder.decode(buf);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read packet data", e);
            }
        }).consumerMainThread((msg, ctx) ->
        {
            NetworkManagerImpl.processMessage(msg, new ForgePacketContext(this.channel, ctx), ctx.get().getDirection().getReceptionSide().isClient() ? this.clientMessageHandler : this.serverMessageHandler);
            ctx.get().setPacketHandled(true);
        });
    }
}
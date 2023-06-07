package com.teamaurora.borealib.impl.network.forge;

import com.teamaurora.borealib.api.network.v1.LoginNetworkChannel;
import com.teamaurora.borealib.api.network.v1.message.MagnetospherePacket;
import com.teamaurora.borealib.api.network.v1.message.PacketDecoder;
import com.teamaurora.borealib.api.network.v1.message.login.MagnetosphereLoginPacket;
import com.teamaurora.borealib.impl.network.NetworkManagerImpl;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.network.HandshakeHandler;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.Internal
public class ForgeLoginChannel extends NetworkChannelImpl implements LoginNetworkChannel {

    public ForgeLoginChannel(SimpleChannel channel, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        super(channel, clientFactory, serverFactory);
    }

    @Override
    public <MSG extends MagnetosphereLoginPacket<T>, T> void register(Class<MSG> clazz, PacketDecoder<MSG, T> decoder) {
        this.channel.messageBuilder(clazz, this.nextId++, NetworkDirection.LOGIN_TO_SERVER)
                .encoder((msg, buf) -> {
                    try {
                        msg.write(buf);
                    } catch (IOException e) {
                        throw new IllegalStateException("Failed to write packet data", e);
                    }
                })
                .decoder(buf -> {
                    try {
                        return decoder.decode(buf);
                    } catch (IOException e) {
                        throw new IllegalStateException("Failed to read packet data", e);
                    }
                })
                .consumerMainThread(HandshakeHandler.indexFirst((__, msg, ctx) ->
                {
                    NetworkManagerImpl.processMessage(msg, new ForgePacketContext(this.channel, ctx), ctx.get().getDirection().getReceptionSide().isClient() ? this.clientMessageHandler : this.serverMessageHandler);
                    ctx.get().setPacketHandled(true);
                }))
                .loginIndex(MagnetosphereLoginPacket::getAsInt, MagnetosphereLoginPacket::setLoginIndex)
                .add();
    }

    @Override
    public <MSG extends MagnetosphereLoginPacket<T>, T> void registerLogin(Class<MSG> clazz, PacketDecoder<MSG, T> decoder, Function<Boolean, List<Pair<String, MSG>>> loginPacketGenerators) {
        this.getMessageBuilder(clazz, decoder, MagnetospherePacket.Direction.LOGIN_CLIENTBOUND)
                .loginIndex(MagnetosphereLoginPacket::getAsInt, MagnetosphereLoginPacket::setLoginIndex)
                .buildLoginPacketList(loginPacketGenerators)
                .add();
    }

    @Override
    public Packet<?> toVanillaPacket(MagnetospherePacket<?> packet, int transactionId, MagnetospherePacket.Direction direction) {
        return this.channel.toVanillaPacket(packet, convert(direction));
    }
}
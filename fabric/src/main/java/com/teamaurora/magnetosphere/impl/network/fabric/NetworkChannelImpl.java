package com.teamaurora.magnetosphere.impl.network.fabric;

import com.google.common.base.Suppliers;
import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import com.teamaurora.magnetosphere.api.network.v1.message.PacketDecoder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@ApiStatus.Internal
public abstract class NetworkChannelImpl {

    protected final ResourceLocation channelId;
    protected final List<MessageFactory<?, ?>> factories;
    protected final Supplier<Object> clientMessageHandler;
    protected final Supplier<Object> serverMessageHandler;

    protected NetworkChannelImpl(ResourceLocation channelId, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        this.channelId = channelId;
        this.factories = new ArrayList<>();
        this.clientMessageHandler = Suppliers.memoize(clientFactory::get);
        this.serverMessageHandler = Suppliers.memoize(serverFactory::get);
    }

    protected FriendlyByteBuf serialize(MagnetospherePacket<?> packet, MagnetospherePacket.Direction expectedDirection) {
        Optional<MessageFactory<?, ?>> factoryOptional = this.factories.stream().filter(factory -> factory.clazz == packet.getClass()).findFirst();
        if (factoryOptional.isEmpty())
            throw new IllegalStateException("Unregistered packet: " + packet.getClass() + " on channel: " + this.channelId);

        int id = this.factories.indexOf(factoryOptional.get());
        if (factoryOptional.get().direction != null && factoryOptional.get().direction != expectedDirection)
            throw new IllegalStateException("Attempted to send packet with id: " + id + ". Expected " + expectedDirection + ", got " + factoryOptional.get().direction);

        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(id);
        try {
            packet.write(buf);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write packet data", e);
        }
        return buf;
    }

    protected MagnetospherePacket<?> deserialize(FriendlyByteBuf buf, MagnetospherePacket.Direction expectedDirection) {
        int id = buf.readVarInt();
        if (id < 0 || id >= this.factories.size())
            throw new IllegalStateException("Unknown packet with id: " + id);

        MessageFactory<?, ?> factory = this.factories.get(id);
        if (factory.direction != null && factory.direction != expectedDirection)
            throw new IllegalStateException("Received unexpected packet with id: " + id + ". Expected " + expectedDirection + ", got " + factory.direction);

        try {
            return factory.decoder.decode(buf);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read packet data", e);
        }
    }

    protected <MSG extends MagnetospherePacket<T>, T> void register(Class<MSG> clazz, PacketDecoder<MSG, T> decoder, @Nullable MagnetospherePacket.Direction direction) {
        this.factories.add(new MessageFactory<>(clazz, decoder, direction));
    }

    private record MessageFactory<MSG extends MagnetospherePacket<T>, T>(Class<MSG> clazz,
                                                                         PacketDecoder<MSG, T> decoder,
                                                                         MagnetospherePacket.Direction direction) {
    }
}
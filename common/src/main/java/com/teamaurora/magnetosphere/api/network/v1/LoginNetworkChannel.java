package com.teamaurora.magnetosphere.api.network.v1;

import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import com.teamaurora.magnetosphere.api.network.v1.message.PacketDecoder;
import com.teamaurora.magnetosphere.api.network.v1.message.login.MagnetosphereLoginPacket;
import net.minecraft.network.protocol.Packet;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface LoginNetworkChannel {

    <MSG extends MagnetosphereLoginPacket<T>, T> void register(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer);

    default <MSG extends MagnetosphereLoginPacket<T>, T> void registerLogin(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer, Supplier<MSG> loginMessageGenerator) {
        this.registerLogin(clazz, deserializer, localChannel -> Collections.singletonList(Pair.of(clazz.getSimpleName(), loginMessageGenerator.get())));
    }

    <MSG extends MagnetosphereLoginPacket<T>, T> void registerLogin(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer, Function<Boolean, List<Pair<String, MSG>>> loginMessageGenerators);

    Packet<?> toVanillaPacket(MagnetospherePacket<?> packet, int transactionId, MagnetospherePacket.Direction direction);
}

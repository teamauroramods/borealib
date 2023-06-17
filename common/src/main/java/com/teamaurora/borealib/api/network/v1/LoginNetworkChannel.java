package com.teamaurora.borealib.api.network.v1;

import com.teamaurora.borealib.api.network.v1.message.BorealibPacket;
import com.teamaurora.borealib.api.network.v1.message.PacketDecoder;
import com.teamaurora.borealib.api.network.v1.message.login.BorealibLoginPacket;
import net.minecraft.network.protocol.Packet;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface LoginNetworkChannel {

    <MSG extends BorealibLoginPacket<T>, T> void register(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer);

    default <MSG extends BorealibLoginPacket<T>, T> void registerLogin(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer, Supplier<MSG> loginMessageGenerator) {
        this.registerLogin(clazz, deserializer, localChannel -> Collections.singletonList(Pair.of(clazz.getSimpleName(), loginMessageGenerator.get())));
    }

    <MSG extends BorealibLoginPacket<T>, T> void registerLogin(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer, Function<Boolean, List<Pair<String, MSG>>> loginMessageGenerators);

    Packet<?> toVanillaPacket(BorealibPacket<?> packet, int transactionId, BorealibPacket.Direction direction);
}

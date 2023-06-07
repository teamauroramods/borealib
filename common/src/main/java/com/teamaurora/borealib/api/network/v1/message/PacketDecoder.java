package com.teamaurora.borealib.api.network.v1.message;

import net.minecraft.network.FriendlyByteBuf;

import java.io.IOException;

public interface PacketDecoder<MSG extends MagnetospherePacket<T>, T> {

    MSG decode(FriendlyByteBuf buf) throws IOException;
}

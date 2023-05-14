package com.teamaurora.magnetosphere.api.network.v1.message.login;

import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import net.minecraft.network.FriendlyByteBuf;

public class ServerboundEmptyResponsePacket extends SimpleMagnetosphereLoginPacket<Object> {

    public ServerboundEmptyResponsePacket() {
    }

    public ServerboundEmptyResponsePacket(FriendlyByteBuf buf) {
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public void read(Object handler, Context ctx) {
    }
}
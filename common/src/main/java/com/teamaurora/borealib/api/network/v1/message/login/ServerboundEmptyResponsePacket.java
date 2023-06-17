package com.teamaurora.borealib.api.network.v1.message.login;

import net.minecraft.network.FriendlyByteBuf;

public class ServerboundEmptyResponsePacket extends SimpleBorealibLoginPacket<Object> {

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
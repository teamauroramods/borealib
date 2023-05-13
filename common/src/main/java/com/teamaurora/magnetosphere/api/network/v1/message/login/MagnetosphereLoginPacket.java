package com.teamaurora.magnetosphere.api.network.v1.message.login;

import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;

import java.util.function.IntSupplier;

public interface MagnetosphereLoginPacket<T> extends MagnetospherePacket<T>, IntSupplier {

    @Override
    int getAsInt();

    void setLoginIndex(int index);
}

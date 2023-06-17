package com.teamaurora.borealib.api.network.v1.message.login;

import com.teamaurora.borealib.api.network.v1.message.BorealibPacket;

import java.util.function.IntSupplier;

public interface BorealibLoginPacket<T> extends BorealibPacket<T>, IntSupplier {

    @Override
    int getAsInt();

    void setLoginIndex(int index);
}

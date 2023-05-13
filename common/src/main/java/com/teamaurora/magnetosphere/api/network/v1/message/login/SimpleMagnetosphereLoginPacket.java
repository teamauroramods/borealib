package com.teamaurora.magnetosphere.api.network.v1.message.login;

public abstract class SimpleMagnetosphereLoginPacket<T> implements MagnetosphereLoginPacket<T> {

    private int loginIndex;

    @Override
    public int getAsInt() {
        return this.loginIndex;
    }

    @Override
    public void setLoginIndex(int index) {
        this.loginIndex = index;
    }
}

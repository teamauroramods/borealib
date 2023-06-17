package com.teamaurora.borealib.api.network.v1.message.login;

public abstract class SimpleBorealibLoginPacket<T> implements BorealibLoginPacket<T> {

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

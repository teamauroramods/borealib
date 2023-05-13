package com.teamaurora.magnetosphere.api.network.v1.message;

import net.minecraft.network.FriendlyByteBuf;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface MagnetospherePacket<T> {

    void write(FriendlyByteBuf buf) throws IOException;

    void read(T handler, Context context);

    interface Context {

        CompletableFuture<Void> enqueueWork(Runnable runnable);

        void waitFor(Future<?> future);

        void reply(MagnetospherePacket<?> packet);

        Direction getDirection();
    }

    enum Direction {

        PLAY_SERVERBOUND,
        PLAY_CLIENTBOUND,
        LOGIN_SERVERBOUND,
        LOGIN_CLIENTBOUND;

        public boolean isClientbound() {
            return this == PLAY_CLIENTBOUND || this == LOGIN_CLIENTBOUND;
        }

        public boolean isServerbound() {
            return this == PLAY_SERVERBOUND || this == LOGIN_SERVERBOUND;
        }
    }
}

package com.teamaurora.borealib.api.network.v1.message;

import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface BorealibPacket<T> {

    void write(FriendlyByteBuf buf) throws IOException;

    void read(T handler, Context context);

    interface Context {

        CompletableFuture<Void> enqueueWork(Runnable runnable);

        void waitFor(Future<?> future);

        void reply(BorealibPacket<?> packet);

        void disconnect(Component message);

        Direction getDirection();

        Connection getNetworkManager();

        @Nullable
        default ServerPlayer getSender() {
            PacketListener netHandler = this.getNetworkManager().getPacketListener();
            if (netHandler instanceof ServerGamePacketListenerImpl netHandlerPlayServer) {
                return netHandlerPlayServer.player;
            }
            return null;
        }
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

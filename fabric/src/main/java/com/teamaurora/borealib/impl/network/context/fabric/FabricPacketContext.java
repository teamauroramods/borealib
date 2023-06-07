package com.teamaurora.borealib.impl.network.context.fabric;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.network.v1.message.MagnetospherePacket;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.util.thread.BlockableEventLoop;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@ApiStatus.Internal
public abstract class FabricPacketContext implements MagnetospherePacket.Context {

    private final ServerLoginNetworking.LoginSynchronizer synchronizer;
    private final MagnetospherePacket.Direction direction;
    private final Connection connection;

    protected FabricPacketContext(Connection connection, ServerLoginNetworking.LoginSynchronizer synchronizer, MagnetospherePacket.Direction direction) {
        this.connection = connection;
        this.synchronizer = synchronizer;
        this.direction = direction;
    }

    @Override
    public CompletableFuture<Void> enqueueWork(Runnable runnable) {
        return (this.direction.isServerbound() ? Platform.getRunningServer().<BlockableEventLoop<?>>map(__ -> __).orElseGet(Platform::getGameExecutor) : Platform.getGameExecutor()).submit(runnable);
    }

    @Override
    public void waitFor(Future<?> future) {
        this.synchronizer.waitFor(future);
    }

    @Override
    public MagnetospherePacket.Direction getDirection() {
        return this.direction;
    }

    @Override
    public void disconnect(Component message) {
        Connection connection = this.getNetworkManager();
        switch (this.getDirection()) {
            case PLAY_SERVERBOUND -> {
                connection.send(new ClientboundDisconnectPacket(message), new PacketSendListener() {
                    @Override
                    public void onSuccess() {
                        connection.disconnect(message);
                    }
                });
                connection.setReadOnly();
                Platform.getRunningServer().ifPresent(server -> server.executeBlocking(connection::handleDisconnection));
            }
            case LOGIN_SERVERBOUND -> {
                connection.send(new ClientboundLoginDisconnectPacket(message), new PacketSendListener() {
                    @Override
                    public void onSuccess() {
                        connection.disconnect(message);
                    }
                });
                connection.setReadOnly();
                Platform.getRunningServer().ifPresent(server -> server.executeBlocking(connection::handleDisconnection));
            }
            case PLAY_CLIENTBOUND, LOGIN_CLIENTBOUND -> connection.disconnect(message);
        }
    }

    @Override
    public Connection getNetworkManager() {
        return this.connection;
    }
}
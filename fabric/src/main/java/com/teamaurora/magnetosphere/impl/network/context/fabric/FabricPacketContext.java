package com.teamaurora.magnetosphere.impl.network.context.fabric;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.util.thread.BlockableEventLoop;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@ApiStatus.Internal
public abstract class FabricPacketContext implements MagnetospherePacket.Context {

    private final ServerLoginNetworking.LoginSynchronizer synchronizer;
    private final MagnetospherePacket.Direction direction;

    protected FabricPacketContext(ServerLoginNetworking.LoginSynchronizer synchronizer, MagnetospherePacket.Direction direction) {
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
}
package com.teamaurora.magnetosphere.impl.fabric;

import com.teamaurora.magnetosphere.api.base.v1.event.events.lifecycle.ServerLifecycleEvents;
import com.teamaurora.magnetosphere.api.base.v1.modloading.fabric.ServicedModInitializer;
import com.teamaurora.magnetosphere.impl.Magnetosphere;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class MagnetosphereFabric implements ServicedModInitializer {

    private static MinecraftServer server;

    @Override
    public String id() {
        return Magnetosphere.MOD_ID;
    }

    @Override
    public void onInitialize() {
        ServicedModInitializer.super.onInitialize();
        ServerLifecycleEvents.PRE_STARTING.register(server1 -> {
            server = server1;
            return true;
        });
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerLifecycleEvents.STOPPING.invoker().forServer(server));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(server1 -> {
            server = null;
            ServerLifecycleEvents.STOPPED.invoker().forServer(server1);
        });
    }

    @Nullable
    public static MinecraftServer getServer() {
        return server;
    }
}

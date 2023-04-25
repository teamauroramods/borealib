package dev.tesseract.impl.fabric;

import dev.tesseract.api.base.v1.event.events.lifecycle.ServerLifecycleEvents;
import dev.tesseract.impl.Tesseract;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class TesseractFabric implements ModInitializer {

    private static MinecraftServer server;

    @Override
    public void onInitialize() {
        Tesseract.INSTANCE.setup();
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

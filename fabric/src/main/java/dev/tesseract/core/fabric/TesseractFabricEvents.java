package dev.tesseract.core.fabric;

import dev.tesseract.api.event.events.lifecycle.ServerLifecycleEvents;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class TesseractFabricEvents {

    private TesseractFabricEvents() {
    }

    public static void init() {
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerLifecycleEvents.STOPPING.invoker().stopping(server));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(server -> ServerLifecycleEvents.STOPPED.invoker().stopped(server));
    }
}

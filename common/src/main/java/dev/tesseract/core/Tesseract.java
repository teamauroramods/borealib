package dev.tesseract.core;

import dev.tesseract.api.event.events.lifecycle.ServerLifecycleEvents;
import dev.tesseract.api.platform.ModInstance;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class Tesseract {
    public static final String MOD_ID = "tesseract";
    public static final ModInstance INSTANCE = ModInstance.builder(MOD_ID)
            .clientInit(() -> Tesseract::clientInit)
            .clientPostInit(() -> Tesseract::clientPostInit)
            .commonInit(Tesseract::commonInit)
            .commonPostInit(Tesseract::commonPostInit)
            .build();

    private static MinecraftServer server;

    private static void clientInit() {
    }

    private static void clientPostInit(ModInstance.ParallelDispatcher dispatcher) {
    }

    private static void commonInit() {
    }

    private static void commonPostInit(ModInstance.ParallelDispatcher dispatcher) {
        ServerLifecycleEvents.PRE_STARTING.register(server -> {
            Tesseract.server = server;
            return true;
        });
        ServerLifecycleEvents.STOPPED.register(server -> Tesseract.server = null);
    }

    @Nullable
    public static MinecraftServer getRunningServer() {
        return server;
    }
}

package dev.tesseract.api.event.events.lifecycle;

import dev.tesseract.api.event.Event;
import dev.tesseract.api.registry.EventRegistry;
import net.minecraft.server.MinecraftServer;

public final class ServerLifecycleEvents {

    public static final Event<PreStart> PRE_STARTING = EventRegistry.createCancellable(PreStart.class);
    public static final Event<Starting> STARTING = EventRegistry.createCancellable(Starting.class);
    public static final Event<Started> STARTED = EventRegistry.createLoop(Started.class);
    public static final Event<Stopping> STOPPING = EventRegistry.createLoop(Stopping.class);
    public static final Event<Stopped> STOPPED = EventRegistry.createLoop(Stopped.class);

    private ServerLifecycleEvents() {
    }

    @FunctionalInterface
    public interface PreStart {
        boolean preStarting(MinecraftServer server);
    }

    @FunctionalInterface
    public interface Starting {
        boolean starting(MinecraftServer server);
    }

    @FunctionalInterface
    public interface Started {
        void started(MinecraftServer server);
    }

    @FunctionalInterface
    public interface Stopping {
        void stopping(MinecraftServer server);
    }
    @FunctionalInterface
    public interface Stopped {
        void stopped(MinecraftServer server);
    }
}

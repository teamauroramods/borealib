package com.teamaurora.magnetosphere.api.base.v1.event.events.lifecycle;

import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import net.minecraft.server.MinecraftServer;

public final class ServerLifecycleEvents {

    /**
     * Fired when the server is about to load the level. This can be cancelled to stop it from starting
     */
    public static final Event<CancellableInstance> PRE_STARTING = Event.createCancellable(CancellableInstance.class);

    /**
     * Fired when the server is currently starting, after loading the level and info. This can be cancelled to stop it from starting
     */
    public static final Event<CancellableInstance> STARTING = Event.createCancellable(CancellableInstance.class);

    /**
     * Fired after the server has fully started.
     */
    public static final Event<Instance> STARTED = Event.createLoop(Instance.class);

    /**
     * Fired when the server starts to stop.
     */
    public static final Event<Instance> STOPPING = Event.createLoop(Instance.class);

    /**
     * Fired when the server has fully stopped.
     */
    public static final Event<Instance> STOPPED = Event.createLoop(Instance.class);

    private ServerLifecycleEvents() {
    }

    @FunctionalInterface
    public interface Instance {
        void forServer(MinecraftServer server);
    }

    @FunctionalInterface
    public interface CancellableInstance {
        boolean forServer(MinecraftServer server);
    }
}
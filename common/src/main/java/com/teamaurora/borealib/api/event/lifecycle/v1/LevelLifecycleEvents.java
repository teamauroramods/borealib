package com.teamaurora.borealib.api.event.lifecycle.v1;

import com.teamaurora.borealib.api.base.v1.event.Event;
import net.minecraft.world.level.LevelAccessor;

public final class LevelLifecycleEvents {

    public static final Event<Load> LOAD = Event.createLoop(Load.class);
    public static final Event<Unload> UNLOAD = Event.createLoop(Unload.class);

    private LevelLifecycleEvents() {
    }

    /**
     * Fired when a level is loaded.
     *
     * @author ebo2022
     * @since 1.0
     */
    @FunctionalInterface
    public interface Load {

        /**
         * Called when the specified level is being loaded.
         *
         * @param level The level being loaded
         */
        void load(LevelAccessor level);
    }

    /**
     * Fired when a level is unloaded.
     *
     * @author ebo2022
     * @since 1.0
     */
    @FunctionalInterface
    public interface Unload {

        /**
         * Called when the specified level is being unloaded.
         *
         * @param level The level being unloaded
         */
        void unload(LevelAccessor level);
    }
}
package com.teamaurora.borealib.api.event.world.v1;

import com.teamaurora.borealib.api.base.v1.event.Event;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.List;

public final class ExplosionEvents {
    public static final Event<Start> START = Event.createCancellable(Start.class);
    public static final Event<Detonate> DETONATE = Event.createLoop(Detonate.class);

    private ExplosionEvents() {
    }

    /**
     * Ran when an explosion is starting.
     *
     * @since 1.0
     */
    @FunctionalInterface
    public interface Start {
        boolean onStart(Level level, Explosion explosion);
    }

    /**
     * Ran when an explosion is detonated and has a list of affected entities that can be altered.
     *
     * @since 1.0
     */
    @FunctionalInterface
    public interface Detonate {
        void detonate(Level level, Explosion explosion, List<Entity> entityList);
    }
}
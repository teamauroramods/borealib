package com.teamaurora.borealib.api.event.config.v1;

import com.teamaurora.borealib.api.base.v1.event.Event;
import com.teamaurora.borealib.api.config.v1.ModConfig;

import java.util.function.Consumer;

/**
 * Events relating to the config lifecycle.
 *
 * @author ebo2022
 * @since 1.0
 */
public final class ConfigEvents {

    /**
     * Fired when a config is loaded.
     */
    public static final Event<Consumer<ModConfig>> LOADING = Event.createLoop(Consumer.class);

    /**
     * Fired when a config is loaded after the initial startup.
     */
    public static final Event<Consumer<ModConfig>> RELOADING = Event.createLoop(Consumer.class);

    private ConfigEvents() {
    }
}

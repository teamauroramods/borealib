package com.teamaurora.magnetosphere.api.event.config.v1;

import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import com.teamaurora.magnetosphere.api.config.v1.ModConfig;

import java.util.function.Consumer;

public final class ConfigEvents {

    public static final Event<Consumer<ModConfig>> LOADING = Event.createLoop(Consumer.class);
    public static final Event<Consumer<ModConfig>> RELOADING = Event.createLoop(Consumer.class);

    private ConfigEvents() {
    }
}

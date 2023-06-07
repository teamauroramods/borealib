package com.teamaurora.borealib.api.event.config.v1;

import com.teamaurora.borealib.api.base.v1.event.Event;
import com.teamaurora.borealib.api.config.v1.ModConfig;

import java.util.function.Consumer;

public final class ConfigEvents {

    public static final Event<Consumer<ModConfig>> LOADING = Event.createLoop(Consumer.class);
    public static final Event<Consumer<ModConfig>> RELOADING = Event.createLoop(Consumer.class);

    private ConfigEvents() {
    }
}

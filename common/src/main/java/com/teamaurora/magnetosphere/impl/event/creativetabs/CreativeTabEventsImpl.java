package com.teamaurora.magnetosphere.impl.event.creativetabs;

import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import com.teamaurora.magnetosphere.api.event.creativetabs.v1.CreativeTabEvents;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@ApiStatus.Internal
public class CreativeTabEventsImpl {

    private static final Map<CreativeModeTab, Event<CreativeTabEvents.ModifyEntries>> EVENTS = new ConcurrentHashMap<>();

    public static Event<CreativeTabEvents.ModifyEntries> event(CreativeModeTab tab) {
        return EVENTS.computeIfAbsent(tab, __ -> Event.createLoop(CreativeTabEvents.ModifyEntries.class));
    }

    @Nullable
    public static Event<CreativeTabEvents.ModifyEntries> get(CreativeModeTab tab) {
        return EVENTS.get(tab);
    }

    public static void forEach(BiConsumer<CreativeModeTab, Event<CreativeTabEvents.ModifyEntries>> consumer) {
        EVENTS.forEach(consumer);
    }
}

package com.teamaurora.magnetosphere.impl.base.util.tabs;

import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import com.teamaurora.magnetosphere.api.base.v1.util.tabs.ModifyCreativeTabEvent;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
public class ModifyCreativeTabEventImpl {

    private static final Map<CreativeModeTab, Event<ModifyCreativeTabEvent>> EVENTS = new ConcurrentHashMap<>();

    public static Event<ModifyCreativeTabEvent> event(CreativeModeTab tab) {
        return EVENTS.computeIfAbsent(tab, __ -> Event.createLoop(ModifyCreativeTabEvent.class));
    }

    @Nullable
    public static Event<ModifyCreativeTabEvent> get(CreativeModeTab tab) {
        return EVENTS.get(tab);
    }
}

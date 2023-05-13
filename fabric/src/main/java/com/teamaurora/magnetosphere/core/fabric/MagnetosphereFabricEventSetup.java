package com.teamaurora.magnetosphere.core.fabric;

import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import com.teamaurora.magnetosphere.api.base.v1.event.events.lifecycle.ServerLifecycleEvents;
import com.teamaurora.magnetosphere.api.base.v1.event.events.misc.CreativeTabEvents;
import com.teamaurora.magnetosphere.impl.base.event.events.misc.CreativeTabEventsImpl;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class MagnetosphereFabricEventSetup {

    public static void init() {
        ServerLifecycleEvents.PRE_STARTING.register(server1 -> {
            MagnetosphereFabric.server = server1;
            return true;
        });
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerLifecycleEvents.STOPPING.invoker().forServer(server));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(server1 -> {
            MagnetosphereFabric.server = null;
            ServerLifecycleEvents.STOPPED.invoker().forServer(server1);
        });
        CreativeTabEventsImpl.forEach((tab, event) -> {
            ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> {
                event.invoker().onModify(entries.getEnabledFeatures(), entries.getContext(), wrapOutput(entries), entries.shouldShowOpRestrictedItems());
            });
        });
    }

    private static CreativeTabEvents.Output wrapOutput(FabricItemGroupEntries entries) {
        return new CreativeTabEvents.Output() {
            @Override
            public void acceptAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                if (after.isEmpty()) {
                    entries.accept(stack, visibility);
                } else {
                    entries.addAfter(after, List.of(stack), visibility);
                }
            }

            @Override
            public void acceptBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                if (before.isEmpty()) {
                    entries.accept(stack, visibility);
                } else {
                    entries.addBefore(before, List.of(stack), visibility);
                }
            }
        };
    }
}

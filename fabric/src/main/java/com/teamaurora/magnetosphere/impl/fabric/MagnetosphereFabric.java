package com.teamaurora.magnetosphere.impl.fabric;

import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import com.teamaurora.magnetosphere.api.base.v1.event.events.lifecycle.ServerLifecycleEvents;
import com.teamaurora.magnetosphere.api.base.v1.modloading.fabric.ServicedModInitializer;
import com.teamaurora.magnetosphere.api.base.v1.util.tabs.ModifyCreativeTabEvent;
import com.teamaurora.magnetosphere.impl.Magnetosphere;
import com.teamaurora.magnetosphere.impl.base.util.tabs.ModifyCreativeTabEventImpl;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ApiStatus.Internal
public class MagnetosphereFabric implements ServicedModInitializer {

    private static MinecraftServer server;

    @Override
    public String id() {
        return Magnetosphere.MOD_ID;
    }

    @Override
    public void onInitialize() {
        ServicedModInitializer.super.onInitialize();
        ServerLifecycleEvents.PRE_STARTING.register(server1 -> {
            server = server1;
            return true;
        });
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerLifecycleEvents.STOPPING.invoker().forServer(server));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(server1 -> {
            server = null;
            ServerLifecycleEvents.STOPPED.invoker().forServer(server1);
        });
        CreativeModeTabs.allTabs().forEach(tab -> {
            Event<ModifyCreativeTabEvent> event = ModifyCreativeTabEventImpl.get(tab);
            if (event != null) {
                ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> {
                    event.invoker().onModify(entries.getEnabledFeatures(), entries.getContext(), new ModifyCreativeTabEvent.Output() {
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
                    }, entries.shouldShowOpRestrictedItems());
                });
            }
        });
    }

    @Nullable
    public static MinecraftServer getServer() {
        return server;
    }
}

package com.teamaurora.magnetosphere.core.fabric;

import com.teamaurora.magnetosphere.api.base.v1.modloading.fabric.MagnetosphereModInitializer;
import com.teamaurora.magnetosphere.api.config.v1.ModConfig;
import com.teamaurora.magnetosphere.api.event.creativetabs.v1.CreativeTabEvents;
import com.teamaurora.magnetosphere.api.event.lifecycle.v1.ServerLifecycleEvents;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import com.teamaurora.magnetosphere.impl.config.fabric.ConfigLoadingHelper;
import com.teamaurora.magnetosphere.impl.config.fabric.ConfigTracker;
import com.teamaurora.magnetosphere.impl.event.creativetabs.CreativeTabEventsImpl;
import com.teamaurora.magnetosphere.impl.registry.fabric.DeferredRegisterImplImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@ApiStatus.Internal
@SuppressWarnings("UnstableApiUsage")
public class MagnetosphereFabric implements MagnetosphereModInitializer {

    static MinecraftServer server;
    private static final LevelResource SERVERCONFIG = new LevelResource("serverconfig");

    @Override
    public String id() {
        return Magnetosphere.MOD_ID;
    }


    @Override
    public void onInitialize() {
        MagnetosphereModInitializer.super.onInitialize();
        ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.COMMON, FabricLoader.getInstance().getConfigDir());
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.CLIENT, FabricLoader.getInstance().getConfigDir());
        ServerLifecycleEvents.PRE_STARTING.register(server1 -> {
            MagnetosphereFabric.server = server1;
            return true;
        });
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.SERVER, ConfigLoadingHelper.getServerConfigDirectory(server));
        });
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerLifecycleEvents.STOPPING.invoker().forServer(server));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(server1 -> {
            MagnetosphereFabric.server = null;
            ConfigTracker.INSTANCE.unloadConfigs(ModConfig.Type.SERVER, ConfigLoadingHelper.getServerConfigDirectory(server1));
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

    @Nullable
    public static MinecraftServer getServer() {
        return server;
    }
}

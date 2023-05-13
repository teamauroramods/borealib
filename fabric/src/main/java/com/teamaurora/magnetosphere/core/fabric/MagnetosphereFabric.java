package com.teamaurora.magnetosphere.core.fabric;

import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import com.teamaurora.magnetosphere.api.base.v1.event.events.lifecycle.ServerLifecycleEvents;
import com.teamaurora.magnetosphere.api.base.v1.modloading.fabric.MagnetosphereModInitializer;
import com.teamaurora.magnetosphere.api.base.v1.event.events.misc.CreativeTabEvents;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import com.teamaurora.magnetosphere.impl.base.event.events.misc.CreativeTabEventsImpl;
import com.teamaurora.magnetosphere.impl.registry.fabric.DeferredRegisterImplImpl;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ApiStatus.Internal
public class MagnetosphereFabric implements MagnetosphereModInitializer {

    static MinecraftServer server;

    @Override
    public String id() {
        return Magnetosphere.MOD_ID;
    }

    @Override
    public void onInitialize() {
        MagnetosphereModInitializer.super.onInitialize();
        DeferredRegisterImplImpl.init();
        MagnetosphereFabricEventSetup.init();
    }

    @Nullable
    public static MinecraftServer getServer() {
        return server;
    }
}

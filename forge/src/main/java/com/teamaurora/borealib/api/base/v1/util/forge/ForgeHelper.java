package com.teamaurora.borealib.api.base.v1.util.forge;

import com.teamaurora.borealib.core.mixin.forge.ModContainerAccessor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public interface ForgeHelper {

    /**
     * Attempts to get the registered configs under a given mod id.
     *
     * @param modId The mod id to get configs for
     * @return The config type to config map if it exists
     */
    static Optional<EnumMap<ModConfig.Type, ModConfig>> getRawConfigData(String modId) {
        return ModList.get().getModContainerById(modId).map(container -> ((ModContainerAccessor) container).getConfigs());
    }

    /**
     * Gets the event bus for the specified mod id.
     *
     * @param modId The mod id
     * @return An event bus for the given mod id
     */
    static IEventBus getEventBus(String modId) {
        return ((FMLModContainer) ModList.get().getModContainerById(modId).orElseThrow(() -> new IllegalArgumentException("Invalid mod: " + modId))).getEventBus();
    }
}

package com.teamaurora.magnetosphere.api.resource_condition.v1;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.api.base.v1.util.NumberComparator;
import com.teamaurora.magnetosphere.api.config.v1.ModConfig;
import com.teamaurora.magnetosphere.impl.resource_condition.DefaultResourceConditionsImpl;
import com.teamaurora.magnetosphere.impl.resource_condition.ResourceConditionRegistryImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface DefaultResourceConditions {

    static ResourceConditionProvider and(ResourceConditionProvider... values) {
        return DefaultResourceConditionsImpl.and(values);
    }

    static ResourceConditionProvider FALSE() {
        return DefaultResourceConditionsImpl.FALSE();
    }

    static ResourceConditionProvider TRUE() {
        return DefaultResourceConditionsImpl.TRUE();
    }

    static ResourceConditionProvider not(ResourceConditionProvider value) {
        return DefaultResourceConditionsImpl.not(value);
    }

    static ResourceConditionProvider or(ResourceConditionProvider... values) {
        return DefaultResourceConditionsImpl.or(values);
    }

    static ResourceConditionProvider registryKeyExists(ResourceKey<? extends Registry<?>> registry, ResourceLocation key) {
        return DefaultResourceConditionsImpl.registryKeyExists(registry, key);
    }

    static ResourceConditionProvider blockExists(ResourceLocation key) {
        return registryKeyExists(Registries.BLOCK, key);
    }

    static ResourceConditionProvider itemExists(ResourceLocation key) {
        return registryKeyExists(Registries.ITEM, key);
    }

    static ResourceConditionProvider fluidExists(ResourceLocation key) {
        return registryKeyExists(Registries.FLUID, key);
    }

    static ResourceConditionProvider allModsLoaded(String... modIds) {
        return DefaultResourceConditionsImpl.allModsLoaded(modIds);
    }

    static ResourceConditionProvider anyModsLoaded(String... modIds) {
        return DefaultResourceConditionsImpl.anyModsLoaded(modIds);
    }

    static ResourceConditionProvider config(String modId, ModConfig.Type type, String key, Object value) {
        return DefaultResourceConditionsImpl.config(modId, type, key, value);
    }

    static ResourceConditionProvider config(String modId, ModConfig.Type type, String key, Number value, NumberComparator comparator) {
        return DefaultResourceConditionsImpl.config(modId, type, key, value, comparator);
    }

}

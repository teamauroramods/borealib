package com.teamaurora.borealib.api.resource_condition.v1;

import com.teamaurora.borealib.api.base.v1.util.NumberComparator;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import com.teamaurora.borealib.impl.resource_condition.DefaultResourceConditionsImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

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

    static ResourceConditionProvider quarkFlag(String flag) {
        return DefaultResourceConditionsImpl.quarkFlag(flag);
    }

    static ResourceConditionProvider woodworksFlag(String value) {
        return DefaultResourceConditionsImpl.woodworksFlag(value);
    }
}

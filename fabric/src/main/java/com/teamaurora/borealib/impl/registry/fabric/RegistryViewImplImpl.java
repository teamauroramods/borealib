package com.teamaurora.borealib.impl.registry.fabric;

import com.teamaurora.borealib.api.registry.v1.RegistryView;
import com.teamaurora.borealib.impl.registry.VanillaRegistryView;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryViewImplImpl {

    private static final Map<ResourceLocation, RegistryView<?>> REGISTRIES = new ConcurrentHashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> RegistryView<T> getRegistry(ResourceLocation id) {
        return (RegistryView<T>) REGISTRIES.computeIfAbsent(id, __ -> {
            Registry<T> vanillaRegistry = (Registry<T>) BuiltInRegistries.REGISTRY.get(id);
            if (vanillaRegistry != null)
                return new VanillaRegistryView<>(vanillaRegistry);
            return null;
        });
    }

    public static Set<Map.Entry<ResourceLocation, RegistryView<?>>> allRegistries() {
        return REGISTRIES.entrySet();
    }
}
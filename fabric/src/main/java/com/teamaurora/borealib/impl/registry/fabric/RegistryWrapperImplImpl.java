package com.teamaurora.borealib.impl.registry.fabric;

import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.impl.registry.VanillaRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryWrapperImplImpl {

    private static final Map<ResourceLocation, RegistryWrapper<?>> REGISTRIES = new ConcurrentHashMap<>();
    private static final Map<String, Map<ResourceKey<? extends Registry<?>>, RegistryWrapper.Provider<?>>> PROVIDERS = new ConcurrentHashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> RegistryWrapper<T> getRegistry(ResourceLocation id) {
        return (RegistryWrapper<T>) REGISTRIES.computeIfAbsent(id, __ -> {
            Registry<T> vanillaRegistry = (Registry<T>) BuiltInRegistries.REGISTRY.get(id);
            if (vanillaRegistry != null)
                return new VanillaRegistryWrapper<>(vanillaRegistry);
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> RegistryWrapper.Provider<T> provider(ResourceKey<? extends Registry<T>> key, String owner) {
        return (RegistryWrapper.Provider<T>) PROVIDERS.computeIfAbsent(owner, __ -> new HashMap<>()).computeIfAbsent(key, __ -> new VanillaRegistryWrapper.Provider<>(key, owner));
    }
}

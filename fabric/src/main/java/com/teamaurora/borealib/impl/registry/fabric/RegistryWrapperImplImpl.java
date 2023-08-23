package com.teamaurora.borealib.impl.registry.fabric;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.mixin.fabric.RegistryDataLoaderAccessor;
import com.teamaurora.borealib.impl.registry.VanillaRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
public class RegistryWrapperImplImpl {

    private static final Map<ResourceLocation, RegistryWrapper<?>> REGISTRIES = new ConcurrentHashMap<>();
    private static final List<RegistryDataLoader.RegistryData<?>> DYNAMIC_REGISTRIES = new ArrayList<>(RegistryDataLoader.WORLDGEN_REGISTRIES);
    private static final Map<ResourceKey<Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> NETWORKABLE_DYNAMIC_REGISTRIES = new HashMap<>();
    private static final Set<ResourceLocation> DYNAMIC_REGISTRY_KEYS = new HashSet<>();

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

    public static <T> RegistryWrapper.Provider<T> provider(ResourceKey<? extends Registry<T>> key, String owner) {
        return new VanillaRegistryWrapper.Provider<>(key, owner);
    }

    public static <T> ResourceKey<? extends Registry<T>> dynamicRegistry(ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        ResourceKey<Registry<T>> key = ResourceKey.createRegistryKey(id);
        DYNAMIC_REGISTRIES.add(new RegistryDataLoader.RegistryData<>(key, codec));
        DYNAMIC_REGISTRY_KEYS.add(key.location());
        if (networkCodec != null)
            NETWORKABLE_DYNAMIC_REGISTRIES.put((ResourceKey) key, new RegistrySynchronization.NetworkedRegistryData<>(key, networkCodec));
        RegistryDataLoaderAccessor.setWorldgenRegistries(DYNAMIC_REGISTRIES);
        return key;
    }

    public static List<RegistryDataLoader.RegistryData<?>> getDynamicRegistries() {
        return DYNAMIC_REGISTRIES;
    }

    public static Map<ResourceKey<Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> getNetworkableDynamicRegistries() {
        return NETWORKABLE_DYNAMIC_REGISTRIES;
    }

    public static String prefix(ResourceLocation location) {
        // Mirrors Forge's directory style
        // Ignores vanilla registries for obvious reasons + any possible non-Borealib dynamic registries whose behavior should be untouched
        if (!location.getNamespace().equals("minecraft") && DYNAMIC_REGISTRY_KEYS.contains(location))
            return location.getNamespace() + "/" + location.getPath();
        return location.getPath();
    }
}

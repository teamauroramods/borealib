package com.teamaurora.magetosphere.impl.registry.forge;

import com.teamaurora.magetosphere.api.registry.v1.PlatformRegistry;
import com.teamaurora.magetosphere.api.registry.v1.RegistryProperties;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
public class RegistryManagerImplImpl {

    private static final Map<ResourceLocation, PlatformRegistry<?>> REGISTRIES = new ConcurrentHashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> PlatformRegistry<T> getRegistry(ResourceLocation id) {
        return (PlatformRegistry<T>) REGISTRIES.computeIfAbsent(id, __ -> {
            IForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(id);
            if (registry != null)
                return new ForgePlatformRegistry<>(registry);
            return null;
        });
    }

    public static <T> PlatformRegistry<T> createRegistry(ResourceLocation id, RegistryProperties<T> properties) {
        if (REGISTRIES.containsKey(id)) throw new IllegalStateException("Attempted to register duplicate registry:" + id);
        PlatformRegistry<T> placeholder = new ForgePlatformRegistry<>(ResourceKey.createRegistryKey(id), properties);
        REGISTRIES.put(id, placeholder);
        return placeholder;
    }
}

package com.teamaurora.borealib.impl.registry.forge;

import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.impl.registry.VanillaRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
public class RegistryWrapperImplImpl {

    private static final Map<ResourceLocation, RegistryWrapper<?>> REGISTRIES = new ConcurrentHashMap<>();
    private static final Map<String, Map<ResourceKey<? extends Registry<?>>, RegistryWrapper.Provider<?>>> PROVIDERS = new ConcurrentHashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> RegistryWrapper<T> getRegistry(ResourceLocation id) {
        return (RegistryWrapper<T>) REGISTRIES.computeIfAbsent(id, __ -> {
            IForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(id);
            if (registry != null)
                return new ForgeRegistryWrapper<>(registry);
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> RegistryWrapper.Provider<T> provider(ResourceKey<? extends Registry<T>> key, String owner) {
        return (RegistryWrapper.Provider<T>) PROVIDERS.computeIfAbsent(owner, __ -> new HashMap<>()).computeIfAbsent(key, __ -> new VanillaRegistryWrapper.Provider<>(key, owner));
    }
}

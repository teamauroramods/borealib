package com.teamaurora.magnetosphere.api.registry.v1;

import com.teamaurora.magnetosphere.impl.registry.RegistryManagerImpl;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * Handles platform-specific registry instances.
 *
 * @since 1.0
 */
public interface RegistryManager {

    /**
     * Wraps a platform-specific registry if it exists.
     *
     * @param registryKey The key of the registry
     * @param <T> The top level type of the registry
     * @return The registry if it exists
     */
    @Nullable
    static <T> PlatformRegistry<T> getRegistry(ResourceKey<? extends Registry<T>> registryKey) {
        return getRegistry(registryKey.location());
    }

    /**
     * Wraps a platform-specific registry if it exists.
     *
     * @param id The name of the registry
     * @param <T> The top level type of the registry
     * @return The registry if it exists
     */
    @Nullable
    static <T> PlatformRegistry<T> getRegistry(ResourceLocation id) {
        return RegistryManagerImpl.getRegistry(id);
    }

    /**
     * Creates a custom registry and registers it to the manager. This wraps each modloader's implementation of extended custom registries.
     *
     * @param id The name of the registry
     * @param properties Properties to control registry behavior
     * @param <T> The top level type of the registry
     * @return The registry if it exists
     */
    static <T> PlatformRegistry<T> createRegistry(ResourceLocation id, RegistryProperties<T> properties) {
        return RegistryManagerImpl.createRegistry(id, properties);
    }
}

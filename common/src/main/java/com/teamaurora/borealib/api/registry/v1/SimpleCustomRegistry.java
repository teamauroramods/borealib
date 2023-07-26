package com.teamaurora.borealib.api.registry.v1;

import com.teamaurora.borealib.impl.registry.VanillaRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * A simplified implementation of {@link RegistryWrapper} for cases where no special functionality is needed. These registries are not tracked by platform APIs.
 * <p>Objects should be registered using {@link #getProvider(String)}.
 * <p>This registry type does NOT support tags.
 *
 * @see RegistryWrapper#createSimple(ResourceLocation)
 * @param <T> The top level registry type
 * @author ebo2022
 * @since 1.0
 */
public final class SimpleCustomRegistry<T> extends VanillaRegistryWrapper<T> {
    private final Map<String, RegistryWrapper.Provider<T>> providers;

    SimpleCustomRegistry(Registry<T> parent) {
        super(parent);
        this.providers = new HashMap<>();
    }

    /**
     * Gets a provider to register objects under the specified owner namespace.
     *
     * @param owner The namespace of the provider
     * @return The provider with the given namespace
     */
    public RegistryWrapper.Provider<T> getProvider(String owner) {
        return this.providers.computeIfAbsent(owner, __ -> new VanillaRegistryWrapper.Provider<>(this.parent, owner));
    }

    /**
     * @return The default provider with the same owner namespace as the registry
     */
    public RegistryWrapper.Provider<T> getDefaultProvider() {
        return this.getProvider(this.key().location().getNamespace());
    }
}
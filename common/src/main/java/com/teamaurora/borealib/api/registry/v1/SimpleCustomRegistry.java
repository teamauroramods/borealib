package com.teamaurora.borealib.api.registry.v1;

import com.teamaurora.borealib.impl.registry.VanillaRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

/**
 * A simplified implementation of {@link RegistryWrapper} for cases where no special functionality is needed.
 * <p>Objects should be registered using {@link #getProvider()}. These registries are not tracked by either platform and are standalone.
 *
 * @see RegistryWrapper#createSimple(ResourceLocation)
 * @param <T> The top level registry type
 * @author ebo2022
 * @since 1.0
 */
public final class SimpleCustomRegistry<T> extends VanillaRegistryWrapper<T> {
    private final RegistryWrapper.Provider<T> provider;

    SimpleCustomRegistry(Registry<T> parent) {
        super(parent);
        this.provider = new VanillaRegistryWrapper.Provider<>(parent, parent.key().location().getNamespace());
    }

    /**
     * @return The provider to register objects to
     */
    public RegistryWrapper.Provider<T> getProvider() {
        return this.provider;
    }
}
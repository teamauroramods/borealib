package com.teamaurora.magetosphere.api.resource_loader.v1;

import com.teamaurora.magetosphere.impl.resource_loader.ResourceLoaderImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

/**
 * Wraps platform-specific resource-related methods, mainly modelled off of Fabric's <code>ResourceManagerHelper</code>.
 *
 * @since 1.0
 */
public interface ResourceLoader {

    static ResourceLoader get(PackType type) {
        return ResourceLoaderImpl.get(type);
    }

    /**
     * Register a reload listener.
     *
     * @param reloadListener The listener to register
     */
    void registerReloadListener(NamedReloadListener reloadListener);

    /**
     * Request that a reload listener be registered before another if possible.
     *
     * @param before The reloader to register before
     * @param after  The reloader to register after
     */
    void addReloaderOrdering(ResourceLocation before, ResourceLocation after);
}

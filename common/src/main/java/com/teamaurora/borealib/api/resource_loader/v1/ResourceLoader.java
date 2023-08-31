package com.teamaurora.borealib.api.resource_loader.v1;

import com.teamaurora.borealib.impl.resource_loader.ResourceLoaderImpl;
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
}

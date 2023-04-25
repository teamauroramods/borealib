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

    void registerReloadListener(ModdedReloadListener reloadListener);

    void addReloaderOrdering(ResourceLocation before, ResourceLocation after);
}

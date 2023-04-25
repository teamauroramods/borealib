package com.teamaurora.magetosphere.api.resource_loader.v1;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

/**
 * An altered reload listener that can be identified by an id.
 *
 * @since 1.0
 */
public interface ModdedReloadListener extends PreparableReloadListener {

    /**
     * @return An id to identify this reload listener by
     */
    ResourceLocation getId();
}

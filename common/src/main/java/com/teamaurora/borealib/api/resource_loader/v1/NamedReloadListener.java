package com.teamaurora.borealib.api.resource_loader.v1;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.Collection;
import java.util.Collections;

/**
 * An altered reload listener that can be identified by an id.
 *
 * @since 1.0
 */
public interface NamedReloadListener extends PreparableReloadListener {

    /**
     * @return An id to identify this reload listener by
     */
    ResourceLocation getId();

    /**
     * @return A list of reload listeners that should be registered before this
     */
    default Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }
}

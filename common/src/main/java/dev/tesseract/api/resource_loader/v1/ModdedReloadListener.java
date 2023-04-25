package dev.tesseract.api.resource_loader.v1;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.concurrent.Executor;

public interface ModdedReloadListener extends PreparableReloadListener {

    /**
     * @return An id to identify this reload listener by
     */
    ResourceLocation getId();
}

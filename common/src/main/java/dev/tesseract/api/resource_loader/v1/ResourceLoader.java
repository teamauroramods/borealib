package dev.tesseract.api.resource_loader.v1;

import dev.tesseract.impl.resource_loader.ResourceLoaderImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public interface ResourceLoader {

    static ResourceLoader get(PackType type) {
        return ResourceLoaderImpl.get(type);
    }

    void registerReloadListener(ModdedReloadListener reloadListener);

    void addReloaderOrdering(ResourceLocation before, ResourceLocation after);
}

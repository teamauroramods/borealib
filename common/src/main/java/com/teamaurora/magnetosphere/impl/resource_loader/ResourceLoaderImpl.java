package com.teamaurora.magnetosphere.impl.resource_loader;

import com.mojang.datafixers.util.Pair;
import com.teamaurora.magnetosphere.api.resource_loader.v1.ResourceLoader;
import dev.architectury.injectables.annotations.ExpectPlatform;
import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
public abstract class ResourceLoaderImpl implements ResourceLoader {

    protected final List<Pair<ResourceLocation, ResourceLocation>> ordering = new ArrayList<>();

    @ExpectPlatform
    public static ResourceLoaderImpl get(PackType packType) {
        return Platform.expect();
    }

    @Override
    public void addReloaderOrdering(ResourceLocation before, ResourceLocation after) {
        Objects.requireNonNull(before, "First reloader key should not be null");
        Objects.requireNonNull(after, "Second reloader key should not be null");
        if (before.equals(after))
            throw new IllegalStateException("Illegal reloader self-dependency:" + before);
        this.ordering.add(Pair.of(before, after));
    }
}
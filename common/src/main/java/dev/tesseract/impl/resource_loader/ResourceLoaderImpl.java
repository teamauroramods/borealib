package dev.tesseract.impl.resource_loader;

import com.mojang.datafixers.util.Pair;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.tesseract.api.base.v1.platform.Platform;
import dev.tesseract.api.resource_loader.v1.ResourceLoader;
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
        return Platform.error();
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

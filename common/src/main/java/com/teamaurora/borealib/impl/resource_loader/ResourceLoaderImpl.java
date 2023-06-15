package com.teamaurora.borealib.impl.resource_loader;

import com.mojang.datafixers.util.Pair;
import com.teamaurora.borealib.api.resource_loader.v1.ResourceLoader;
import dev.architectury.injectables.annotations.ExpectPlatform;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
public abstract class ResourceLoaderImpl implements ResourceLoader {

    @ExpectPlatform
    public static ResourceLoaderImpl get(PackType packType) {
        return Platform.expect();
    }
}

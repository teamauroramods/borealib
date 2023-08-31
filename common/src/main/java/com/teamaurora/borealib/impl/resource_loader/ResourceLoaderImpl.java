package com.teamaurora.borealib.impl.resource_loader;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.resource_loader.v1.ResourceLoader;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class ResourceLoaderImpl implements ResourceLoader {

    @ExpectPlatform
    public static ResourceLoaderImpl get(PackType packType) {
        return Platform.expect();
    }
}

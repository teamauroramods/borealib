package com.teamaurora.magnetosphere.impl.registry;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class RegistryViewImpl {

    @ExpectPlatform
    public static <T> RegistryView<T> getRegistry(ResourceLocation id) {
        return Platform.expect();
    }

}

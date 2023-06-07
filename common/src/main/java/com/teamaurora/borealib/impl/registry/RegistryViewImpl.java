package com.teamaurora.borealib.impl.registry;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.registry.v1.RegistryView;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Set;

@ApiStatus.Internal
public final class RegistryViewImpl {

    @ExpectPlatform
    public static <T> RegistryView<T> getRegistry(ResourceLocation id) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static Set<Map.Entry<ResourceLocation, RegistryView<?>>> allRegistries() {
        return Platform.expect();
    }
}

package dev.tesseract.impl.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.tesseract.api.base.v1.platform.Platform;
import dev.tesseract.api.registry.v1.PlatformRegistry;
import dev.tesseract.api.registry.v1.RegistryProperties;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class RegistryManagerImpl {

    @Nullable
    @ExpectPlatform
    public static <T> PlatformRegistry<T> getRegistry(ResourceLocation id) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static <T> PlatformRegistry<T> createRegistry(ResourceLocation id, RegistryProperties<T> properties) {
        return Platform.expect();
    }
}

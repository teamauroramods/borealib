package dev.tesseract.api.registry.v1;

import dev.tesseract.impl.registry.RegistryManagerImpl;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface RegistryManager {

    @Nullable
    static <T> PlatformRegistry<T> getRegistry(ResourceKey<? extends Registry<T>> registryKey) {
        return getRegistry(registryKey.location());
    }

    @Nullable
    static <T> PlatformRegistry<T> getRegistry(ResourceLocation id) {
        return RegistryManagerImpl.getRegistry(id);
    }

    static <T> PlatformRegistry<T> createRegistry(ResourceLocation id, RegistryProperties<T> properties) {
        return RegistryManagerImpl.createRegistry(id, properties);
    }
}

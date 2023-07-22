package com.teamaurora.borealib.impl.registry;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Set;

@ApiStatus.Internal
public final class RegistryWrapperImpl {

    @ExpectPlatform
    public static <T> RegistryWrapper<T> getRegistry(ResourceLocation id) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static <T> RegistryWrapper.Provider<T> provider(ResourceKey<? extends Registry<T>> key, String owner) {
        return Platform.expect();
    }

    public abstract static class Provider<T> implements RegistryWrapper.Provider<T> {

        protected final ResourceKey<? extends Registry<T>> registryKey;
        protected final String modId;

        protected Provider(ResourceKey<? extends Registry<T>> registryKey, String modId) {
            this.registryKey = registryKey;
            this.modId = modId;
        }

        @Override
        public String owner() {
            return this.modId;
        }

        @Override
        public ResourceKey<? extends Registry<T>> getRegistryKey() {
            return this.registryKey;
        }
    }
}

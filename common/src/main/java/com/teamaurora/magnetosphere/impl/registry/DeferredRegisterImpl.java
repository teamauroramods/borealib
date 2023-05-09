package com.teamaurora.magnetosphere.impl.registry;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.api.registry.v1.DeferredRegister;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class DeferredRegisterImpl<T> implements DeferredRegister<T> {

    protected final ResourceKey<? extends Registry<T>> registryKey;
    protected final String modId;

    @ExpectPlatform
    public static <T> DeferredRegisterImpl<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return Platform.expect();
    }

    protected DeferredRegisterImpl(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        this.registryKey = registryKey;
        this.modId = modId;
    }

    @Override
    public String id() {
        return this.modId;
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }
}

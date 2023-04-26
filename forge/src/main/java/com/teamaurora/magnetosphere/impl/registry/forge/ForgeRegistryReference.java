package com.teamaurora.magnetosphere.impl.registry.forge;

import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ForgeRegistryReference<T> implements RegistryReference<T> {

    private final RegistryObject<T> registryObject;
    private final ResourceKey<? extends Registry<T>> registryKey;

    ForgeRegistryReference(RegistryObject<T> registryObject, ResourceKey<? extends Registry<T>> registryKey) {
        this.registryObject = registryObject;
        this.registryKey = registryKey;
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }

    @Override
    public ResourceKey<T> getKey() {
        return this.registryObject.getKey();
    }

    @Override
    public ResourceLocation getId() {
        return this.registryObject.getId();
    }

    @Override
    public T get() {
        return this.registryObject.get();
    }

    @Override
    public Holder<T> getHolder() {
        return this.registryObject.getHolder().orElseThrow();
    }

    @Override
    public boolean isPresent() {
        return this.registryObject.isPresent();
    }
}
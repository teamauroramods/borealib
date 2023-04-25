package com.teamaurora.magetosphere.impl.registry;

import com.teamaurora.magetosphere.api.registry.v1.RegistryReference;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * implementation for Fabric
 * @param <T> type of the referenced object
 */
public final class VanillaRegistryReference<T> implements RegistryReference<T> {

    private final T value;
    private final ResourceKey<T> key;
    private final Registry<T> registry;

    public VanillaRegistryReference(T value, ResourceKey<T> key, Registry<T> registry) {
        this.value = value;
        this.key = key;
        this.registry = registry;
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registry.key();
    }

    @Override
    public ResourceKey<T> getKey() {
        return this.key;
    }

    @Override
    public ResourceLocation getId() {
        return this.key.location();
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public Holder<T> getHolder() {
        return this.registry.getHolderOrThrow(this.key);
    }

    @Override
    public boolean isPresent() {
        return true;
    }
}
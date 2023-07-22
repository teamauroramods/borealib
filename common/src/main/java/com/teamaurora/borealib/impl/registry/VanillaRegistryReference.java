package com.teamaurora.borealib.impl.registry;

import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

@ApiStatus.Internal
public class VanillaRegistryReference<R extends T, T> implements RegistryReference<R> {
    private final ResourceLocation id;
    private final ResourceKey<R> key;
    private final R value;
    private Holder<R> holder;
    private final ResourceKey<? extends Registry<T>> registryKey;

    public VanillaRegistryReference(ResourceLocation id, ResourceKey<? extends Registry<T>> registryKey, R value) {
        this.value = value;
        this.id = id;
        this.registryKey = registryKey;
        this.key = (ResourceKey<R>) ResourceKey.create(registryKey, id);
    }

    @Override
    public R get() {
        return this.value;
    }

    @Override
    public boolean isPresent() {
        return this.value != null;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public ResourceKey<R> getKey() {
        return this.key;
    }

    @Override
    public Optional<Holder<R>> getHolder() {
        return Optional.ofNullable(this.holder);
    }
}
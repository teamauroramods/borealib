package com.teamaurora.magnetosphere.api.registry.v1;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface DeferredRegister<T> extends Iterable<RegistryReference<T>> {


    String id();

    ResourceKey<? extends Registry<T>> getRegistryKey();

    <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object);

    default <R extends T> RegistryReference<R> register(String path, Supplier<? extends R> object) {
        return this.register(new ResourceLocation(this.id(), path), object);
    }

    default <R extends T> RegistryReference<R> registerConditional(ResourceLocation name, Supplier<? extends R> dummy, Supplier<? extends R> object, boolean register) {
        return this.register(name, register ? object : dummy);
    }

    default <R extends T> RegistryReference<R> registerConditional(String path, Supplier<? extends R> dummy, Supplier<? extends R> object, boolean register) {
        return this.register(path, register ? object : dummy);
    }

    Supplier<RegistryView<T>> makeRegistry(Supplier<RegistryProperties<T>> properties);
}

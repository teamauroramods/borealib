package com.teamaurora.magnetosphere.impl.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import com.teamaurora.magnetosphere.api.registry.v1.PlatformRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class VanillaPlatformRegistry<T> implements PlatformRegistry<T> {

    private final Registry<T> parent;

    public VanillaPlatformRegistry(Registry<T> parent) {
        this.parent = parent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object) {
        return (RegistryReference<R>) new VanillaRegistryReference<>(Registry.register(this.parent, name, object.get()), ResourceKey.create(this.key(), name), this.parent);
    }

    @Override
    public Codec<T> byNameCodec() {
        return this.parent.byNameCodec();
    }

    @Override
    public @Nullable ResourceLocation getKey(T value) {
        return this.parent.getKey(value);
    }

    @Override
    public int getId(@Nullable T value) {
        return this.parent.getId(value);
    }

    @Override
    public @Nullable T byId(int id) {
        return this.parent.byId(id);
    }

    @Override
    public int size() {
        return this.parent.size();
    }

    @Override
    public @Nullable T get(@Nullable ResourceLocation name) {
        return this.parent.get(name);
    }

    @Override
    public ResourceKey<? extends Registry<T>> key() {
        return this.parent.key();
    }

    @Override
    public Set<ResourceLocation> keySet() {
        return this.parent.keySet();
    }

    @Override
    public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
        return this.parent.entrySet();
    }

    @Override
    public boolean containsKey(ResourceLocation name) {
        return this.parent.containsKey(name);
    }

    @Override
    public <T1> Stream<T1> keys(DynamicOps<T1> ops) {
        return this.parent.keys(ops);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this.parent.iterator();
    }
}

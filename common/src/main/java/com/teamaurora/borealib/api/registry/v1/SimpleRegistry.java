package com.teamaurora.borealib.api.registry.v1;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterators;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

/**
 * A simplified implementation of {@link RegistryWrapper} for cases where no special functionality is needed. These registries are not tracked by platform APIs.
 * <p>It is suggested that classes holding objects from these registries have a static <code>init()</code> method called on common init to ensure they are initialized.
 *
 * @see RegistryWrapper#createSimple(ResourceLocation)
 * @param <T> The top level registry type
 * @author ebo2022
 * @since 1.0
 */
public final class SimpleRegistry<T> implements RegistryWrapper<T> {
    private final BiMap<ResourceLocation, T> byLocation = HashBiMap.create();
    private final BiMap<ResourceKey<T>, T> byKey = HashBiMap.create();
    private final BiMap<Integer, T> byId = HashBiMap.create();
    private final MutableInt nextId = new MutableInt();
    private final ResourceKey<? extends Registry<T>> key;
    private final Codec<T> codec;

    SimpleRegistry(ResourceLocation name) {
        this.key = ResourceKey.createRegistryKey(name);
        this.codec = new Codec<>() {
            @Override
            public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
                return ResourceLocation.CODEC.decode(ops, input).flatMap((pair) -> {
                    ResourceLocation name = pair.getFirst();
                    T value = SimpleRegistry.this.get(name);
                    return value == null ? DataResult.error(() -> "Unknown registry key: " + name) : DataResult.success(Pair.of(value, pair.getSecond()), Lifecycle.stable());
                });
            }

            @Override
            public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
                ResourceLocation name = SimpleRegistry.this.getKey(input);
                if (name == null) {
                    return DataResult.error(() -> "Unknown registry element: " + prefix);
                }
                return ops.mergeToPrimitive(prefix, ops.createString(name.toString())).setLifecycle(Lifecycle.stable());
            }
        };
    }

    public <R extends T> R register(ResourceLocation name, R value) {
        this.byLocation.put(name, value);
        this.byKey.put(ResourceKey.create(this.key, name), value);
        this.byId.put(this.nextId.getAndIncrement(), value);
        return value;
    }

    @Override
    public Codec<T> byNameCodec() {
        return this.codec;
    }

    @Override
    @Nullable
    public ResourceLocation getKey(T value) {
        return this.byLocation.inverse().get(value);
    }

    @Override
    public Optional<ResourceKey<T>> getResourceKey(T value) {
        return Optional.ofNullable(this.byKey.inverse().get(value));
    }

    @Override
    public int getId(@Nullable T value) {
        return Objects.requireNonNullElse(this.byId.inverse().get(value), IdMap.DEFAULT);
    }

    @Override
    @Nullable
    public T byId(int id) {
        return id >= 0 && id < this.byId.size() ? this.byId.get(id) : null;
    }

    @Override
    public int size() {
        return this.byKey.size();
    }

    @Override
    @Nullable
    public T get(@Nullable ResourceLocation name) {
        return this.byLocation.get(name);
    }

    @Override
    public ResourceKey<? extends Registry<T>> key() {
        return this.key;
    }

    @Override
    public Set<ResourceLocation> keySet() {
        return Collections.unmodifiableSet(this.byLocation.keySet());
    }

    @Override
    public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
        return Collections.unmodifiableSet(this.byKey.entrySet());
    }

    @Override
    public boolean containsKey(ResourceLocation name) {
        return this.byLocation.containsKey(name);
    }

    @Override
    public Optional<Holder<T>> getHolder(T object) {
        return Optional.empty();
    }

    @Override
    public <T1> Stream<T1> keys(DynamicOps<T1> ops) {
        return this.keySet().stream().map(rl -> ops.createString(rl.toString()));
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return Iterators.unmodifiableIterator(this.byKey.values().iterator());
    }
}
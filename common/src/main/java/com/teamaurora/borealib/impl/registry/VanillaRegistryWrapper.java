package com.teamaurora.borealib.impl.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryTagManager;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ApiStatus.Internal
public class VanillaRegistryWrapper<T> implements RegistryWrapper<T> {

    protected final Registry<T> parent;
    @Nullable
    private final RegistryTagManager<T> tagManager;

    public VanillaRegistryWrapper(Registry<T> parent, @Nullable RegistryTagManager<T> tagManager) {
        this.parent = parent;
        this.tagManager = tagManager;
    }

    @Override
    public Codec<T> byNameCodec() {
        return this.parent.byNameCodec();
    }

    @Override
    @Nullable
    public RegistryTagManager<T> tags() {
        return this.tagManager;
    }

    @Override
    public @Nullable ResourceLocation getKey(T value) {
        return this.parent.getKey(value);
    }

    @Override
    public Optional<ResourceKey<T>> getResourceKey(T value) {
        return this.parent.getResourceKey(value);
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

    @SuppressWarnings("unchecked")
    public static class Provider<T> extends RegistryWrapperImpl.Provider<T> {

        private final Map<RegistryReference<T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
        private final Registry<T> registry;

        public Provider(ResourceKey<? extends Registry<T>> registryKey, String modId) {
            super(registryKey, modId);
            this.registry = (Registry<T>) Objects.requireNonNull(BuiltInRegistries.REGISTRY.get(this.registryKey.location()));
        }

        public Provider(Registry<T> registry, String modId) {
            super(registry.key(), modId);
            this.registry = registry;
        }

        @Override
        public <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object) {
            R registered = Registry.register(this.registry, name, object.get());
            return new VanillaRegistryReference<>(name, this.registryKey, registered);
        }

        @NotNull
        @Override
        public Iterator<RegistryReference<T>> iterator() {
            return this.entries.keySet().iterator();
        }
    }
}

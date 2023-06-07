package com.teamaurora.borealib.impl.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.teamaurora.borealib.api.registry.v1.RegistryView;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class VanillaRegistryView<T> implements RegistryView<T> {

    private final Registry<T> parent;

    public VanillaRegistryView(Registry<T> parent) {
        this.parent = parent;
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
    public Iterable<T> getTagOrEmpty(TagKey<T> tagKey) {
        List<T> values = new ArrayList<>();
        this.parent.getTagOrEmpty(tagKey).forEach(h -> values.add(h.value()));
        return values;
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

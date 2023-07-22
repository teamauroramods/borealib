package com.teamaurora.borealib.impl.registry.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class ForgeRegistryWrapper<T> implements RegistryWrapper<T> {

    private final IForgeRegistry<T> registry;

    // constructor for existing registries
    ForgeRegistryWrapper(IForgeRegistry<T> registry) {
        this.registry = registry;
    }

    @Override
    public Codec<T> byNameCodec() {
        return this.registry.getCodec();
    }

    @Override
    @Nullable
    public ResourceLocation getKey(T value) {
        return this.registry.getKey(value);
    }

    @Override
    public Optional<ResourceKey<T>> getResourceKey(T value) {
        return this.registry.getResourceKey(value);
    }

    @Override
    public int getId(@Nullable T value) {
        return ((ForgeRegistry<T>) this.registry).getID(value);
    }

    @Override
    @Nullable
    public T byId(int id) {
        return ((ForgeRegistry<T>) this.registry).getValue(id);
    }

    @Override
    public int size() {
        return this.registry.getEntries().size();
    }

    @Override
    @Nullable
    public T get(@Nullable ResourceLocation name) {
        return this.registry.getValue(name);
    }

    @Override
    public ResourceKey<? extends Registry<T>> key() {
        return this.registry.getRegistryKey();
    }

    @Override
    public Set<ResourceLocation> keySet() {
        return this.registry.getKeys();
    }

    @Override
    public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
        return this.registry.getEntries();
    }

    @Override
    public Iterable<T> getTagOrEmpty(TagKey<T> tagKey) {
        ITagManager<T> tags = this.registry.tags();
        if (tags != null) {
            return tags.getTag(tagKey);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean containsKey(ResourceLocation name) {
        return this.registry.containsKey(name);
    }

    @Override
    public <T1> Stream<T1> keys(DynamicOps<T1> ops) {
        return this.keySet().stream().map(rl -> ops.createString(rl.toString()));
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this.registry.iterator();
    }
}
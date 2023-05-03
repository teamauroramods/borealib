package com.teamaurora.magnetosphere.impl.registry.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class ForgeRegistryView<T> implements RegistryView<T> {

    private final IForgeRegistry<T> registry;

    // constructor for existing registries
    ForgeRegistryView(IForgeRegistry<T> registry) {
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

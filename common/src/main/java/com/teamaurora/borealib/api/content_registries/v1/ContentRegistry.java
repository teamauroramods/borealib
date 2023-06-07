package com.teamaurora.borealib.api.content_registries.v1;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.registry.v1.RegistryView;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.BiConsumer;

public interface ContentRegistry<T, R> {

    RegistryView<T> registry();

    ResourceLocation id();

    @Nullable
    R get(T entry);

    @Nullable
    R getDirect(T entry);

    @Nullable
    R getByTag(TagKey<T> tagKey);

    default ResourceKey<? extends Registry<T>> key() {
        return this.registry().key();
    }

    void forEach(BiConsumer<T, R> consumer);

    Set<T> keySet();

    Set<R> values();

    Codec<R> elementCodec();
}
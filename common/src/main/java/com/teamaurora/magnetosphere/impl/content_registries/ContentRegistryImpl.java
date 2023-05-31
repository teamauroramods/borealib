package com.teamaurora.magnetosphere.impl.content_registries;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.teamaurora.magnetosphere.api.content_registries.v1.ContentRegistry;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ContentRegistryImpl<T, R> implements ContentRegistry<T, R> {

    private final ResourceLocation id;
    private final RegistryView<T> registry;
    private final Codec<R> elementCodec;
    final Map<T, R> byValue;
    final Map<TagKey<T>, R> byTag;

    ContentRegistryImpl(ResourceLocation id, RegistryView<T> registry, Codec<R> elementCodec) {
        this.id = id;
        this.registry = registry;
        this.elementCodec = elementCodec;
        this.byValue = new HashMap<>();
        this.byTag = new HashMap<>();
    }

    @Override
    public RegistryView<T> registry() {
        return this.registry;
    }

    @Override
    public ResourceLocation id() {
        return this.id;
    }

    @Override
    @Nullable
    public R get(T value) {
        R entry = this.getDirect(value);
        if (entry == null) {
            for (Map.Entry<TagKey<T>, R> tagEntry : this.byTag.entrySet()) {
                for (T tagValue : this.registry.getTagOrEmpty(tagEntry.getKey())) {
                    if (tagValue.equals(value)) {
                        if (entry != null)
                            Magnetosphere.LOGGER.warn("Overriding value " + this.registry.getKey(value) + " of content registry " + this.id + " with value defined by parent tag " + tagEntry.getKey().toString());
                        entry = tagEntry.getValue();
                    }
                }
            }
        }
        return entry;
    }

    @Override
    @Nullable
    public R getDirect(T value) {
        return this.byValue.get(value);
    }

    @Override
    @Nullable
    public R get(TagKey<T> value) {
        return this.byTag.get(value);
    }

    @Override
    public void forEach(BiConsumer<T, R> consumer) {
        this.keySet().forEach(object -> consumer.accept(object, this.get(object)));
    }

    @Override
    public Set<T> keySet() {
        return this.registry.stream().filter(object -> Objects.nonNull(object) && this.get(object) != null).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<R> values() {
        return this.registry.stream().map(this::get).filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Codec<R> elementCodec() {
        return this.elementCodec;
    }
}

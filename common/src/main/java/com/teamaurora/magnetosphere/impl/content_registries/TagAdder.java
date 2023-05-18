package com.teamaurora.magnetosphere.impl.content_registries;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

@ApiStatus.Internal
public class TagAdder<T> {

    private final Map<ResourceLocation, Collection<Holder<T>>> map;
    private final ResourceKey<? extends Registry<T>> key;

    public TagAdder(TagManager.LoadResult<T> value) {
        this.map = new HashMap<>(value.tags());
        this.key = value.key();
    }

    public void add(ResourceLocation tag, Collection<Holder<T>> values) {
        if (map.containsKey(tag)) {
            List<Holder<T>> tagsList = new ArrayList<>(map.get(tag));
            for (Holder<T> value : values)
                if (!tagsList.contains(value))
                    tagsList.add(value);
            map.replace(tag, tagsList);
        }
    }

    public Collection<Holder<T>> get(ResourceLocation tag) {
            return this.map.get(tag);
    }

    public List<TagKey<T>> keySet() {
        return this.map.keySet().stream().map(id -> TagKey.create(key, id)).toList();
    }

    public ResourceKey<? extends Registry<T>> key() {
        return key;
    }

    public Map<ResourceLocation, Collection<Holder<T>>> asMap() {
        return map;
    }
}
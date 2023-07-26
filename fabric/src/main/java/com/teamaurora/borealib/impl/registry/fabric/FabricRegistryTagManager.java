package com.teamaurora.borealib.impl.registry.fabric;

import com.google.common.collect.Iterators;
import com.teamaurora.borealib.api.registry.v1.RegistryTagManager;
import com.teamaurora.borealib.core.extensions.MappedRegistryExtension;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ApiStatus.Internal
public class FabricRegistryTagManager<T> implements RegistryTagManager<T> {

    private final Registry<T> owner;
    private volatile Map<TagKey<T>, TagData<T>> tags = new IdentityHashMap<>();

    public FabricRegistryTagManager(Registry<T> owner) {
        this.owner = owner;
    }

    public void bind(Map<TagKey<T>, HolderSet.Named<T>> holderTags) {
        IdentityHashMap<TagKey<T>, TagData<T>> newTags = new IdentityHashMap<>(this.tags);
        // Forcefully unbind all pre-existing tags
        newTags.values().forEach(tag -> ((TagDataImpl<T>) tag).bind(null));
        // Bind all tags that were loaded
        holderTags.forEach((key, holderSet) -> ((TagDataImpl<T>) newTags.computeIfAbsent(key, TagDataImpl::new)).bind(holderSet));
        this.tags = newTags;
    }

    @Override
    public TagData<T> getTag(TagKey<T> tagKey) {
        TagData<T> tag = this.tags.get(tagKey);
        if (tag == null) {
            tag = new TagDataImpl<>(tagKey);
            IdentityHashMap<TagKey<T>, TagData<T>> map = new IdentityHashMap<>(this.tags);
            map.put(tagKey, tag);
            this.tags = map;
        }
        return tag;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Optional<ReverseTagData<T>> getReverseTag(T object) {
        return (Optional<ReverseTagData<T>>) (Optional) ((MappedRegistryExtension<T>) this.owner).borealib$getHolder(object);
    }

    @Override
    public boolean isKnownTagName(TagKey<T> tagKey) {
        TagData<T> tag = this.tags.get(tagKey);
        return tag != null && tag.isBound();
    }

    @Override
    public Stream<TagData<T>> stream() {
        return this.tags.values().stream();
    }

    @Override
    public Stream<TagKey<T>> getTagNames() {
        return this.tags.keySet().stream();
    }

    @Override
    public TagKey<T> createTagKey(ResourceLocation name) {
        return TagKey.create(this.owner.key(), name);
    }

    @Override
    public TagKey<T> createOptionalTagKey(ResourceLocation name, Set<? extends Supplier<T>> defaultValues) {
        TagKey<T> tagKey = this.createTagKey(name);
        this.addOptionalTagDefaults(tagKey, defaultValues);
        return tagKey;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addOptionalTagDefaults(TagKey<T> tagKey, Set<? extends Supplier<T>> defaultValues) {
        ((MappedRegistryExtension<T>) this.owner).borealib$addOptionalTag(tagKey, defaultValues);
    }

    @NotNull
    @Override
    public Iterator<TagData<T>> iterator() {
        return Iterators.unmodifiableIterator(this.tags.values().iterator());
    }

    public static class TagDataImpl<T> implements TagData<T> {
        private final TagKey<T> key;
        @Nullable
        private HolderSet<T> holderSet;
        @Nullable
        private List<T> contents;

        TagDataImpl(TagKey<T> key) {
            this.key = key;
        }

        @Override
        public TagKey<T> getKey() {
            return this.key;
        }

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return this.getContents().iterator();
        }

        @Override
        public Spliterator<T> spliterator() {
            return this.getContents().spliterator();
        }

        @Override
        public boolean isEmpty() {
            return this.getContents().isEmpty();
        }

        @Override
        public int size() {
            return this.getContents().size();
        }

        @Override
        public Stream<T> stream() {
            return this.getContents().stream();
        }

        @Override
        public boolean contains(T value) {
            return this.getContents().contains(value);
        }

        @Override
        public Optional<T> getRandomElement(RandomSource random) {
            return Util.getRandomSafe(this.getContents(), random);
        }

        @Override
        public boolean isBound() {
            return this.holderSet != null;
        }

        List<T> getContents() {
            if (this.contents == null && this.holderSet != null)
                this.contents = this.holderSet.stream().map(Holder::value).toList();
            return this.contents == null ? List.of() : this.contents;
        }

        void bind(@Nullable HolderSet<T> holderSet) {
            this.holderSet = holderSet;
            this.contents = null;
        }

        @Override
        public String toString() {
            return "Tag[" +
                    "key=" + this.key + ", " +
                    "contents=" + getContents() + ']';
        }
    }
}

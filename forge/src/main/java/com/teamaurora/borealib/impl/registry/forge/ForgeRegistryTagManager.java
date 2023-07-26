package com.teamaurora.borealib.impl.registry.forge;

import com.google.common.collect.Iterators;
import com.teamaurora.borealib.api.registry.v1.RegistryTagManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraftforge.registries.tags.IReverseTag;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ApiStatus.Internal
public record ForgeRegistryTagManager<T>(ITagManager<T> parent) implements RegistryTagManager<T> {

    @Override
    public TagData<T> getTag(TagKey<T> tagKey) {
        return new TagDataWrapper<>(this.parent.getTag(tagKey));
    }

    @Override
    public Optional<ReverseTagData<T>> getReverseTag(T object) {
        return this.parent.getReverseTag(object).map(ReverseTagDataWrapper::new);
    }

    @Override
    public boolean isKnownTagName(TagKey<T> tagKey) {
        return this.parent.isKnownTagName(tagKey);
    }

    @Override
    public Stream<TagData<T>> stream() {
        return this.parent.stream().map(TagDataWrapper::new);
    }

    @Override
    public Stream<TagKey<T>> getTagNames() {
        return this.parent.getTagNames();
    }

    @Override
    public TagKey<T> createTagKey(ResourceLocation name) {
        return this.parent.createTagKey(name);
    }

    @Override
    public TagKey<T> createOptionalTagKey(ResourceLocation name, Set<? extends Supplier<T>> defaultValues) {
        return this.parent.createOptionalTagKey(name, defaultValues);
    }

    @Override
    public void addOptionalTagDefaults(TagKey<T> tagKey, Set<? extends Supplier<T>> defaultValues) {
        this.parent.addOptionalTagDefaults(tagKey, defaultValues);
    }

    @NotNull
    @Override
    public Iterator<TagData<T>> iterator() {
        return Iterators.transform(this.parent.iterator(), TagDataWrapper::new);
    }

    private record TagDataWrapper<T>(ITag<T> parent) implements TagData<T> {

        @Override
        public TagKey<T> getKey() {
            return this.parent.getKey();
        }

        @Override
        public Stream<T> stream() {
            return this.parent.stream();
        }

        @Override
        public boolean isEmpty() {
            return this.parent.isEmpty();
        }

        @Override
        public int size() {
            return this.parent.size();
        }

        @Override
        public boolean contains(T object) {
            return this.parent.contains(object);
        }

        @Override
        public Optional<T> getRandomElement(RandomSource random) {
            return this.parent.getRandomElement(random);
        }

        @Override
        public boolean isBound() {
            return this.parent.isBound();
        }

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return this.parent.iterator();
        }
    }
    private record ReverseTagDataWrapper<T>(IReverseTag<T> parent) implements ReverseTagData<T> {

        @Override
        public Stream<TagKey<T>> getTagKeys() {
            return this.parent.getTagKeys();
        }

        @Override
        public boolean containsTag(TagKey<T> tagKey) {
            return this.parent.containsTag(tagKey);
        }
    }
}
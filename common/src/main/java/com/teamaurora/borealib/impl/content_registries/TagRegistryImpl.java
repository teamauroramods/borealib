package com.teamaurora.borealib.impl.content_registries;

import com.mojang.datafixers.util.Pair;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
@SuppressWarnings({"unchecked", "rawtypes"})
public class TagRegistryImpl {

    private static final Map<ResourceKey<? extends Registry<?>>, Set<Pair<TagKey<?>, TagKey<?>>>> REGISTRY = new ConcurrentHashMap<>();

    private static <T> Set<Pair<TagKey<T>, TagKey<T>>> listTags(ResourceKey<? extends Registry<T>> key) {
        return (Set) REGISTRY.computeIfAbsent(key, __ -> new HashSet<>());
    }

    public static <T> void syncTags(ResourceKey<? extends Registry<T>> key, TagKey<T> tag1, TagKey<T> tag2) {
        if(!listTags(key).add(Pair.of(tag1, tag2)))
            Borealib.LOGGER.warn("Duplicate tag sync registration: " + tag1 + " <-> " + tag2);
    }

    public static <T> TagManager.LoadResult<T> handle(TagAdder<T> adder) {
       for (TagKey<T> tagKey : adder.keySet()) {
           ResourceLocation id = tagKey.location();
           for (Pair<TagKey<T>, TagKey<T>> pair : listTags(adder.key())) {
               if (pair.getSecond().equals(tagKey)) {
                   TagKey<T> otherTag = pair.getFirst();
                   ResourceLocation otherId = otherTag.location();
                   Borealib.LOGGER.debug("Syncing tags: " + id + " <-> " + otherId);
                   adder.add(otherId, adder.get(id));
                   if (adder.keySet().contains(otherTag))
                       adder.add(id, adder.get(otherId));
                   break;
               }
               if (pair.getFirst().equals(tagKey)) {
                   TagKey<T> otherTag = pair.getSecond();
                   ResourceLocation otherId = otherTag.location();
                   Borealib.LOGGER.debug("Syncing tags: " + id + " <-> " + otherId);
                   adder.add(otherId, adder.get(id));
                   if (adder.keySet().contains(otherTag))
                       adder.add(id, adder.get(otherId));
                   break;
               }
           }
       }
       return new TagManager.LoadResult<>(adder.key(), adder.a
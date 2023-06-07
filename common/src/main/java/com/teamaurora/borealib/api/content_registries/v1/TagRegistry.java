package com.teamaurora.borealib.api.content_registries.v1;

import com.teamaurora.borealib.impl.content_registries.TagRegistryImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface TagRegistry {

    static <T> TagKey<T> bind(ResourceKey<? extends Registry<T>> registry, ResourceLocation name) {
        return TagKey.create(registry, name);
    }

    static <T> TagKey<T> bind(Registry<T> registry, ResourceLocation name) {
        return TagKey.create(registry.key(), name);
    }

    static TagKey<Item> bindItem(ResourceLocation name) {
        return bind(Registries.ITEM, name);
    }

    static TagKey<Block> bindBlock(ResourceLocation name) {
        return bind(Registries.BLOCK, name);
    }

    static TagKey<EntityType<?>> bindEntityType(ResourceLocation name) {
        return bind(Registries.ENTITY_TYPE, name);
    }

    static TagKey<Fluid> bindFluid(ResourceLocation name) {
        return bind(Registries.FLUID, name);
    }

    static <T> void syncTags(ResourceKey<? extends Registry<T>> key, TagKey<T> tag1, TagKey<T> tag2) {
        TagRegistryImpl.syncTags(key, tag1, tag2);
    }
}

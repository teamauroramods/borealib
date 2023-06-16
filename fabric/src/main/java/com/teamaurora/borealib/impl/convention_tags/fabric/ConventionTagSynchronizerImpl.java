package com.teamaurora.borealib.impl.convention_tags.fabric;

import com.teamaurora.borealib.api.content_registries.v1.TagRegistry;
import com.teamaurora.borealib.api.convention_tags.v1.PlatformBiomeTags;
import com.teamaurora.borealib.api.convention_tags.v1.PlatformBlockTags;
import com.teamaurora.borealib.api.convention_tags.v1.PlatformEntityTypeTags;
import com.teamaurora.borealib.api.convention_tags.v1.PlatformItemTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ConventionTagSynchronizerImpl {
    public static void init() {
        ResourceKey<Registry<Block>> blocks = Registries.BLOCK;
        TagRegistry.syncTags(blocks, PlatformBlockTags.BARRELS_WOODEN, ConventionalBlockTags.WOODEN_BARRELS);
        TagRegistry.syncTags(blocks, PlatformBlockTags.ORES, ConventionalBlockTags.ORES);
        TagRegistry.syncTags(blocks, PlatformBlockTags.ORES_QUARTZ, ConventionalBlockTags.QUARTZ_ORES);
        TagRegistry.syncTags(blocks, PlatformBlockTags.BOOKSHELVES, ConventionalBlockTags.BOOKSHELVES);
        TagRegistry.syncTags(blocks, PlatformBlockTags.CHESTS, ConventionalBlockTags.CHESTS);
        TagRegistry.syncTags(blocks, PlatformBlockTags.GLASS, ConventionalBlockTags.GLASS_BLOCKS);
        TagRegistry.syncTags(blocks, PlatformBlockTags.GLASS_PANES, ConventionalBlockTags.GLASS_PANES);

        ResourceKey<Registry<Item>> items = Registries.ITEM;
        TagRegistry.syncTags(items, PlatformItemTags.SHEARS, ConventionalItemTags.SHEARS);
        TagRegistry.syncTags(items, PlatformItemTags.INGOTS_IRON, ConventionalItemTags.IRON_INGOTS);
        TagRegistry.syncTags(items, PlatformItemTags.RAW_MATERIALS_IRON, ConventionalItemTags.RAW_IRON_ORES);
        TagRegistry.syncTags(items, PlatformItemTags.STORAGE_BLOCKS_RAW_IRON, ConventionalItemTags.RAW_IRON_BLOCKS);
        TagRegistry.syncTags(items, PlatformItemTags.RAW_MATERIALS_GOLD, ConventionalItemTags.RAW_GOLD_ORES);
        TagRegistry.syncTags(items, PlatformItemTags.STORAGE_BLOCKS_RAW_GOLD, ConventionalItemTags.RAW_GOLD_BLOCKS);
        TagRegistry.syncTags(items, PlatformItemTags.INGOTS_GOLD, ConventionalItemTags.GOLD_INGOTS);
        TagRegistry.syncTags(items, PlatformItemTags.DUSTS_REDSTONE, ConventionalItemTags.REDSTONE_DUSTS);
        TagRegistry.syncTags(items, PlatformItemTags.INGOTS_COPPER, ConventionalItemTags.COPPER_INGOTS);
        TagRegistry.syncTags(items, PlatformItemTags.RAW_MATERIALS_COPPER, ConventionalItemTags.RAW_COPPER_ORES);
        TagRegistry.syncTags(items, PlatformItemTags.STORAGE_BLOCKS_RAW_COPPER, ConventionalItemTags.RAW_COPPER_BLOCKS);
        TagRegistry.syncTags(items, PlatformItemTags.ORES, ConventionalItemTags.ORES);
        TagRegistry.syncTags(items, PlatformItemTags.INGOTS_NETHERITE, ConventionalItemTags.NETHERITE_INGOTS);
        TagRegistry.syncTags(items, PlatformItemTags.ORES_QUARTZ, ConventionalItemTags.QUARTZ_ORES);
        TagRegistry.syncTags(items, PlatformItemTags.GEMS_QUARTZ, ConventionalItemTags.QUARTZ);
        TagRegistry.syncTags(items, PlatformItemTags.GEMS_LAPIS, ConventionalItemTags.LAPIS);
        TagRegistry.syncTags(items, PlatformItemTags.GEMS_DIAMOND, ConventionalItemTags.DIAMONDS);
        TagRegistry.syncTags(items, PlatformItemTags.GEMS_EMERALD, ConventionalItemTags.EMERALDS);
        TagRegistry.syncTags(items, PlatformItemTags.GLASS, ConventionalItemTags.GLASS_BLOCKS);
        TagRegistry.syncTags(items, PlatformItemTags.GLASS_PANES, ConventionalItemTags.GLASS_PANES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES, ConventionalItemTags.DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_BLACK, ConventionalItemTags.BLACK_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_BLUE, ConventionalItemTags.BLUE_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_BROWN, ConventionalItemTags.BROWN_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_CYAN, ConventionalItemTags.CYAN_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_GRAY, ConventionalItemTags.GRAY_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_GREEN, ConventionalItemTags.GREEN_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_LIGHT_BLUE, ConventionalItemTags.LIGHT_BLUE_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_LIGHT_GRAY, ConventionalItemTags.LIGHT_GRAY_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_LIME, ConventionalItemTags.LIME_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_MAGENTA, ConventionalItemTags.MAGENTA_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_ORANGE, ConventionalItemTags.ORANGE_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_PINK, ConventionalItemTags.PINK_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_PURPLE, ConventionalItemTags.PURPLE_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_RED, ConventionalItemTags.RED_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_WHITE, ConventionalItemTags.WHITE_DYES);
        TagRegistry.syncTags(items, PlatformItemTags.DYES_YELLOW, ConventionalItemTags.YELLOW_DYES);

        ResourceKey<Registry<Biome>> biomes = Registries.BIOME;
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_HOT, ConventionalBiomeTags.CLIMATE_HOT);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_COLD, ConventionalBiomeTags.CLIMATE_COLD);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_UNDERGROUND, ConventionalBiomeTags.UNDERGROUND);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_SLOPE, ConventionalBiomeTags.MOUNTAIN_SLOPE);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_PEAK, ConventionalBiomeTags.MOUNTAIN_PEAK);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_MOUNTAIN, ConventionalBiomeTags.MOUNTAIN);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_CAVE, ConventionalBiomeTags.CAVES);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_CONIFEROUS, ConventionalBiomeTags.TREE_CONIFEROUS);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_WASTELAND, ConventionalBiomeTags.WASTELAND);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_SWAMP, ConventionalBiomeTags.SWAMP);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_MUSHROOM, ConventionalBiomeTags.MUSHROOM);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_DENSE_OVERWORLD, ConventionalBiomeTags.VEGETATION_DENSE);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_SPARSE_OVERWORLD, ConventionalBiomeTags.VEGETATION_SPARSE);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_DRY, ConventionalBiomeTags.CLIMATE_DRY);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_WET, ConventionalBiomeTags.CLIMATE_WET);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_DESERT, ConventionalBiomeTags.DESERT);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_DEAD, ConventionalBiomeTags.DEAD);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_SNOWY, ConventionalBiomeTags.SNOWY);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_WATER, ConventionalBiomeTags.AQUATIC);
        TagRegistry.syncTags(biomes, PlatformBiomeTags.IS_VOID, ConventionalBiomeTags.VOID);

        ResourceKey<Registry<EntityType<?>>> entities = Registries.ENTITY_TYPE;
        TagRegistry.syncTags(entities, PlatformEntityTypeTags.BOSSES, ConventionalEntityTypeTags.BOSSES);
    }
}
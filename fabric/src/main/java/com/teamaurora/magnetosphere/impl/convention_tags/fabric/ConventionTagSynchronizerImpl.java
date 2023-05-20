package com.teamaurora.magnetosphere.impl.convention_tags.fabric;

import com.teamaurora.magnetosphere.api.content_registries.v1.TagRegistry;
import com.teamaurora.magnetosphere.api.convention_tags.v1.MagnetosphereBiomeTags;
import com.teamaurora.magnetosphere.api.convention_tags.v1.MagnetosphereBlockTags;
import com.teamaurora.magnetosphere.api.convention_tags.v1.MagnetosphereEntityTypeTags;
import com.teamaurora.magnetosphere.api.convention_tags.v1.MagnetosphereItemTags;
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
    public static void syncBlocks() {

    }

    public static void init() {
        ResourceKey<Registry<Block>> blocks = Registries.BLOCK;
        TagRegistry.syncTags(blocks, MagnetosphereBlockTags.ORES, ConventionalBlockTags.ORES);
        TagRegistry.syncTags(blocks, MagnetosphereBlockTags.ORES_QUARTZ, ConventionalBlockTags.QUARTZ_ORES);
        TagRegistry.syncTags(blocks, MagnetosphereBlockTags.BOOKSHELVES, ConventionalBlockTags.BOOKSHELVES);
        TagRegistry.syncTags(blocks, MagnetosphereBlockTags.CHESTS, ConventionalBlockTags.CHESTS);
        TagRegistry.syncTags(blocks, MagnetosphereBlockTags.GLASS, ConventionalBlockTags.GLASS_BLOCKS);
        TagRegistry.syncTags(blocks, MagnetosphereBlockTags.GLASS_PANES, ConventionalBlockTags.GLASS_PANES);

        ResourceKey<Registry<Item>> items = Registries.ITEM;
        TagRegistry.syncTags(items, MagnetosphereItemTags.SHEARS, ConventionalItemTags.SHEARS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.INGOTS_IRON, ConventionalItemTags.IRON_INGOTS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.RAW_MATERIALS_IRON, ConventionalItemTags.RAW_IRON_ORES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.STORAGE_BLOCKS_RAW_IRON, ConventionalItemTags.RAW_IRON_BLOCKS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.RAW_MATERIALS_GOLD, ConventionalItemTags.RAW_GOLD_ORES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.STORAGE_BLOCKS_RAW_GOLD, ConventionalItemTags.RAW_GOLD_BLOCKS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.INGOTS_GOLD, ConventionalItemTags.GOLD_INGOTS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DUSTS_REDSTONE, ConventionalItemTags.REDSTONE_DUSTS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.INGOTS_COPPER, ConventionalItemTags.COPPER_INGOTS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.RAW_MATERIALS_COPPER, ConventionalItemTags.RAW_COPPER_ORES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.STORAGE_BLOCKS_RAW_COPPER, ConventionalItemTags.RAW_COPPER_BLOCKS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.ORES, ConventionalItemTags.ORES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.INGOTS_NETHERITE, ConventionalItemTags.NETHERITE_INGOTS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.ORES_QUARTZ, ConventionalItemTags.QUARTZ_ORES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.GEMS_QUARTZ, ConventionalItemTags.QUARTZ);
        TagRegistry.syncTags(items, MagnetosphereItemTags.GEMS_LAPIS, ConventionalItemTags.LAPIS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.GEMS_DIAMOND, ConventionalItemTags.DIAMONDS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.GEMS_EMERALD, ConventionalItemTags.EMERALDS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.GLASS, ConventionalItemTags.GLASS_BLOCKS);
        TagRegistry.syncTags(items, MagnetosphereItemTags.GLASS_PANES, ConventionalItemTags.GLASS_PANES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES, ConventionalItemTags.DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_BLACK, ConventionalItemTags.BLACK_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_BLUE, ConventionalItemTags.BLUE_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_BROWN, ConventionalItemTags.BROWN_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_CYAN, ConventionalItemTags.CYAN_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_GRAY, ConventionalItemTags.GRAY_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_GREEN, ConventionalItemTags.GREEN_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_LIGHT_BLUE, ConventionalItemTags.LIGHT_BLUE_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_LIGHT_GRAY, ConventionalItemTags.LIGHT_GRAY_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_LIME, ConventionalItemTags.LIME_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_MAGENTA, ConventionalItemTags.MAGENTA_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_ORANGE, ConventionalItemTags.ORANGE_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_PINK, ConventionalItemTags.PINK_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_PURPLE, ConventionalItemTags.PURPLE_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_RED, ConventionalItemTags.RED_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_WHITE, ConventionalItemTags.WHITE_DYES);
        TagRegistry.syncTags(items, MagnetosphereItemTags.DYES_YELLOW, ConventionalItemTags.YELLOW_DYES);

        ResourceKey<Registry<Biome>> biomes = Registries.BIOME;
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_HOT, ConventionalBiomeTags.CLIMATE_HOT);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_COLD, ConventionalBiomeTags.CLIMATE_COLD);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_UNDERGROUND, ConventionalBiomeTags.UNDERGROUND);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_SLOPE, ConventionalBiomeTags.MOUNTAIN_SLOPE);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_PEAK, ConventionalBiomeTags.MOUNTAIN_PEAK);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_MOUNTAIN, ConventionalBiomeTags.MOUNTAIN);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_CAVE, ConventionalBiomeTags.CAVES);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_CONIFEROUS, ConventionalBiomeTags.TREE_CONIFEROUS);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_WASTELAND, ConventionalBiomeTags.WASTELAND);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_SWAMP, ConventionalBiomeTags.SWAMP);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_MUSHROOM, ConventionalBiomeTags.MUSHROOM);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_DENSE_OVERWORLD, ConventionalBiomeTags.VEGETATION_DENSE);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_SPARSE_OVERWORLD, ConventionalBiomeTags.VEGETATION_SPARSE);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_DRY, ConventionalBiomeTags.CLIMATE_DRY);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_WET, ConventionalBiomeTags.CLIMATE_WET);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_DESERT, ConventionalBiomeTags.DESERT);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_DEAD, ConventionalBiomeTags.DEAD);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_SNOWY, ConventionalBiomeTags.SNOWY);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_WATER, ConventionalBiomeTags.AQUATIC);
        TagRegistry.syncTags(biomes, MagnetosphereBiomeTags.IS_VOID, ConventionalBiomeTags.VOID);

        ResourceKey<Registry<EntityType<?>>> entities = Registries.ENTITY_TYPE;
        TagRegistry.syncTags(entities, MagnetosphereEntityTypeTags.BOSSES, ConventionalEntityTypeTags.BOSSES);
    }
}

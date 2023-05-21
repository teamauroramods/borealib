package com.teamaurora.magnetosphere.api.datagen.v1.providers.loot;

import com.teamaurora.magnetosphere.api.block.v1.set.wood.WoodSet;
import com.teamaurora.magnetosphere.api.block.v1.set.wood.WoodVariants;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;

import java.util.Set;

public abstract class MagnetosphereBlockLootSubProvider extends BlockLootSubProvider {

    protected MagnetosphereBlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet featureFlags) {
        super(explosionResistant, featureFlags);
    }

    protected MagnetosphereBlockLootSubProvider(Set<Item> explosionResistant) {
        this(explosionResistant, FeatureFlags.REGISTRY.allFlags());
    }

    protected void createWoodSetTables(WoodSet woodSet) {
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.PLANKS).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.LOG).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.WOOD).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.STRIPPED_LOG).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.STRIPPED_WOOD).get());
        woodSet.variant(WoodVariants.SAPLING).ifPresent(b -> this.dropSelf(b.get()));
        woodSet.variant(WoodVariants.POTTED_SAPLING).ifPresent(b -> this.dropPottedContents(b.get()));
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.STANDING_SIGN).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.PRESSURE_PLATE).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.FENCE).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.TRAPDOOR).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.FENCE_GATE).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.BUTTON).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.STAIRS).get());
        this.add(woodSet.variantOrThrow(WoodVariants.SLAB).get(), this::createSlabItemTable);
        this.add(woodSet.variantOrThrow(WoodVariants.DOOR).get(), this::createDoorTable);
        woodSet.variant(WoodVariants.LEAVES).ifPresent(b -> this.add(b.get(), b1 -> this.createLeavesDrops(b1, woodSet.variantOrThrow(WoodVariants.SAPLING).get(), NORMAL_LEAVES_SAPLING_CHANCES)));
    }
}

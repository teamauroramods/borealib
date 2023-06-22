package com.teamaurora.borealib.api.datagen.v1.providers.loot;

import com.teamaurora.borealib.api.block.v1.compat.CommonCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Set;

/**
 * A {@link BlockLootSubProvider} extension for extra functionality.
 *
 * @author ebo2022
 * @since 1.0
 */
public abstract class BorealibBlockLootSubProvider extends BlockLootSubProvider {

    protected BorealibBlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet featureFlags) {
        super(explosionResistant, featureFlags);
    }

    protected BorealibBlockLootSubProvider(Set<Item> explosionResistant) {
        this(explosionResistant, FeatureFlags.REGISTRY.allFlags());
    }

    /**
     * Generates block loot for the specified {@link WoodSet}.
     *
     * @param woodSet The block set to generate loot tables for
     */
    protected void createWoodSetTables(WoodSet woodSet) {
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.PLANKS).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.LOG).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.WOOD).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.STRIPPED_LOG).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.STRIPPED_WOOD).get());
        woodSet.variant(WoodVariants.SAPLING).ifPresent(b -> this.dropSelf(b.get()));
        woodSet.variant(WoodVariants.POTTED_SAPLING).ifPresent(b -> this.dropPottedContents(b.get()));
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.STANDING_SIGN).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.HANGING_SIGN).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.PRESSURE_PLATE).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.FENCE).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.TRAPDOOR).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.FENCE_GATE).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.BUTTON).get());
        this.dropSelf(woodSet.variantOrThrow(WoodVariants.STAIRS).get());
        this.add(woodSet.variantOrThrow(WoodVariants.SLAB).get(), this::createSlabItemTable);
        this.add(woodSet.variantOrThrow(WoodVariants.DOOR).get(), this::createDoorTable);
        woodSet.variant(WoodVariants.LEAVES).ifPresent(b -> this.add(b.get(), b1 -> this.createLeavesDrops(b1, woodSet.variantOrThrow(WoodVariants.SAPLING).get(), NORMAL_LEAVES_SAPLING_CHANCES)));
        this.bookshelf(woodSet.variantOrThrow(CommonCompatBlockVariants.BOOKSHELF).get());
        this.add(woodSet.variantOrThrow(CommonCompatBlockVariants.WOODEN_CHEST).get(), this::createNameableBlockEntityTable);
        this.add(woodSet.variantOrThrow(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST).get(), this::createNameableBlockEntityTable);
    }

    protected void bookshelf(Block bookshelf) {
        this.add(bookshelf, block -> createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(Items.BOOK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))))));
    }
}

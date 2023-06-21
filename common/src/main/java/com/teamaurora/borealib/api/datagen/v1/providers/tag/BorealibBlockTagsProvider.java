package com.teamaurora.borealib.api.datagen.v1.providers.tag;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.base.v1.util.Mods;
import com.teamaurora.borealib.api.block.v1.compat.CommonCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public abstract class BorealibBlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {

    public BorealibBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, Registries.BLOCK, lookup, block -> block.builtInRegistryHolder().key());
    }

    protected void generateForWoodSet(WoodSet woodSet) {

        IntrinsicTagAppender<Block> mineableWithAxe = this.tag(BlockTags.MINEABLE_WITH_AXE);
        IntrinsicTagAppender<Block> mineableWithHoe = this.tag(BlockTags.MINEABLE_WITH_HOE);
        TagKey<Block> logsTag = woodSet.getBlockLogTag();

        Block chest = woodSet.variantOrThrow(CommonCompatBlockVariants.WOODEN_CHEST).get();
        Block trappedChest = woodSet.variantOrThrow(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST).get();
        Block bookshelf = woodSet.variantOrThrow(CommonCompatBlockVariants.BOOKSHELF).get();

        // Log tag & mineables
        this.tag(logsTag).add(woodSet.variantOrThrow(WoodVariants.LOG).get(), woodSet.variantOrThrow(WoodVariants.WOOD).get(), woodSet.variantOrThrow(WoodVariants.STRIPPED_LOG).get(), woodSet.variantOrThrow(WoodVariants.STRIPPED_WOOD).get());
        WoodSet.DEFAULT_VARIANTS.stream().map(variant -> woodSet.variantOrThrow(variant).get()).forEach(mineableWithAxe::add);
        mineableWithAxe.add(chest, trappedChest, bookshelf);
        mineableWithHoe.add(woodSet.variantOrThrow(WoodVariants.LEAVES).get());

        // Common tags
        this.tag(BlockTags.FENCE_GATES).add(woodSet.variantOrThrow(WoodVariants.FENCE_GATE).get());
        woodSet.variant(WoodVariants.POTTED_SAPLING).ifPresent(block -> this.tag(BlockTags.FLOWER_POTS).add(block.get()));
        this.tag(BlockTags.LOGS_THAT_BURN).addTag(logsTag);
        this.tag(BlockTags.PLANKS).add(woodSet.variantOrThrow(WoodVariants.PLANKS).get());
        woodSet.variant(WoodVariants.SAPLING).ifPresent(block -> this.tag(BlockTags.SAPLINGS).add(block.get()));
        this.tag(BlockTags.STANDING_SIGNS).add(woodSet.variantOrThrow(WoodVariants.STANDING_SIGN).get());
        this.tag(BlockTags.WALL_SIGNS).add(woodSet.variantOrThrow(WoodVariants.WALL_SIGN).get());
        this.tag(BlockTags.WOODEN_BUTTONS).add(woodSet.variantOrThrow(WoodVariants.BUTTON).get());
        this.tag(BlockTags.WOODEN_DOORS).add(woodSet.variantOrThrow(WoodVariants.DOOR).get());
        this.tag(BlockTags.WOODEN_FENCES).add(woodSet.variantOrThrow(WoodVariants.FENCE).get());
        this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(woodSet.variantOrThrow(WoodVariants.PRESSURE_PLATE).get());
        this.tag(BlockTags.WOODEN_SLABS).add(woodSet.variantOrThrow(WoodVariants.SLAB).get());
        this.tag(BlockTags.WOODEN_STAIRS).add(woodSet.variantOrThrow(WoodVariants.STAIRS).get());
        this.tag(BlockTags.WOODEN_TRAPDOORS).add(woodSet.variantOrThrow(WoodVariants.TRAPDOOR).get());

        // Common compat
        this.tag(Mods.CARPENTER.blockTag("chests")).add(chest);
        this.tag(Mods.CARPENTER.blockTag("trapped_chests")).add(trappedChest);
        this.tag(Mods.CARPENTER.blockTag("bookshelves")).add(bookshelf);

        // Fabric & Forge-specific compat
        if (Platform.isFabric()) {
            this.tag(Mods.FABRIC_TAGS.blockTag("chests")).add(chest, trappedChest);
            this.tag(Mods.FABRIC_TAGS.blockTag("bookshelves")).add(bookshelf);
        } else if (Platform.isForge()) {
            this.tag(Mods.FORGE.blockTag("chests/wooden")).add(chest);
            this.tag(Mods.FORGE.blockTag("chests/trapped")).add(trappedChest);
            this.tag(Mods.FORGE.blockTag("bookshelves")).add(bookshelf);
        }
    }
}

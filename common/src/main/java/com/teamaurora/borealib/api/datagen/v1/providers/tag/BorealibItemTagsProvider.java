package com.teamaurora.borealib.api.datagen.v1.providers.tag;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.base.v1.util.Mods;
import com.teamaurora.borealib.api.block.v1.compat.CommonCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public abstract class BorealibItemTagsProvider extends ItemTagsProvider {
    public BorealibItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, BorealibBlockTagsProvider blockTagsProvider) {
        super(packOutput, completableFuture,  blockTagsProvider.contentsGetter());
    }

    protected void generateForWoodSet(WoodSet woodSet) {

        TagKey<Item> logsTag = woodSet.getItemLogTag();

        Item chest = getAsItem(woodSet.variantOrThrow(CommonCompatBlockVariants.WOODEN_CHEST));
        Item trappedChest = getAsItem(woodSet.variantOrThrow(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST));
        Item bookshelf = getAsItem(woodSet.variantOrThrow(CommonCompatBlockVariants.BOOKSHELF));

        // Log tag
        this.tag(logsTag).add(getAsItem(woodSet.variantOrThrow(WoodVariants.LOG)), getAsItem(woodSet.variantOrThrow(WoodVariants.WOOD)), getAsItem(woodSet.variantOrThrow(WoodVariants.STRIPPED_LOG)), getAsItem(woodSet.variantOrThrow(WoodVariants.STRIPPED_WOOD)));

        // Common tags
        this.tag(ItemTags.BOATS).add(woodSet.itemVariantOrThrow(WoodVariants.BOAT).get());
        this.tag(ItemTags.CHEST_BOATS).add(woodSet.itemVariantOrThrow(WoodVariants.CHEST_BOAT).get());
        this.tag(ItemTags.FENCE_GATES).add(getAsItem(woodSet.variantOrThrow(WoodVariants.FENCE_GATE)));
        this.tag(ItemTags.LOGS_THAT_BURN).addTag(logsTag);
        this.tag(ItemTags.PLANKS).add(getAsItem(woodSet.variantOrThrow(WoodVariants.PLANKS)));
        woodSet.variant(WoodVariants.SAPLING).ifPresent(block -> this.tag(ItemTags.SAPLINGS).add(getAsItem(block)));
        this.tag(ItemTags.SIGNS).add(getAsItem(woodSet.variantOrThrow(WoodVariants.STANDING_SIGN)));
        this.tag(ItemTags.WOODEN_BUTTONS).add(getAsItem(woodSet.variantOrThrow(WoodVariants.BUTTON)));
        this.tag(ItemTags.WOODEN_DOORS).add(getAsItem(woodSet.variantOrThrow(WoodVariants.DOOR)));
        this.tag(ItemTags.WOODEN_FENCES).add(getAsItem(woodSet.variantOrThrow(WoodVariants.FENCE)));
        this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(getAsItem(woodSet.variantOrThrow(WoodVariants.PRESSURE_PLATE)));
        this.tag(ItemTags.WOODEN_SLABS).add(getAsItem(woodSet.variantOrThrow(WoodVariants.SLAB)));
        this.tag(ItemTags.WOODEN_STAIRS).add(getAsItem(woodSet.variantOrThrow(WoodVariants.STAIRS)));
        this.tag(ItemTags.WOODEN_TRAPDOORS).add(getAsItem(woodSet.variantOrThrow(WoodVariants.TRAPDOOR)));

        // Common compat
        this.tag(Mods.CARPENTER.itemTag("chests")).add(chest);
        this.tag(Mods.CARPENTER.itemTag("trapped_chests")).add(trappedChest);
        this.tag(Mods.CARPENTER.itemTag("bookshelves")).add(bookshelf);

        // Fabric & Forge-specific compat
        if (Platform.isFabric()) {
            this.tag(Mods.FABRIC_TAGS.itemTag("chests")).add(chest, trappedChest);
            this.tag(Mods.FABRIC_TAGS.itemTag("bookshelves")).add(bookshelf);
        } else if (Platform.isForge()) {
            this.tag(Mods.FORGE.itemTag("chests/wooden")).add(chest);
            this.tag(Mods.FORGE.itemTag("chests/trapped")).add(trappedChest);
            this.tag(Mods.FORGE.itemTag("bookshelves")).add(bookshelf);
            this.tag(Mods.QUARK.itemTag("boatable_chests")).add(chest);
            this.tag(Mods.QUARK.itemTag("revertable_chests")).add(chest);
        }
    }

    private static Item getAsItem(RegistryReference<Block> registryReference) {
        return registryReference.get().asItem();
    }
}

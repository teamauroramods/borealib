package com.teamaurora.borealib.api.datagen.v1.providers.tag;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.base.v1.util.Mods;
import com.teamaurora.borealib.api.block.v1.compat.CommonCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.core.mixin.ItemTagsProviderAccessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
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
        this.copyIfNeeded(woodSet.getBlockLogTag(), woodSet.getItemLogTag());

        // Common tags
        this.tag(ItemTags.BOATS).add(woodSet.itemVariantOrThrow(WoodVariants.BOAT).get());
        this.tag(ItemTags.CHEST_BOATS).add(woodSet.itemVariantOrThrow(WoodVariants.CHEST_BOAT).get());
        this.tag(ItemTags.LEAVES).add(getAsItem(woodSet.variantOrThrow(WoodVariants.LEAVES)));
        this.tag(ItemTags.SIGNS).add(woodSet.itemVariantOrThrow(WoodVariants.SIGN_ITEM).get());
        this.tag(ItemTags.HANGING_SIGNS).add(woodSet.itemVariantOrThrow(WoodVariants.HANGING_SIGN_ITEM).get());

        this.copyIfNeeded(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        this.copyIfNeeded(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        woodSet.variant(WoodVariants.SAPLING).ifPresent(__ -> this.copyIfNeeded(BlockTags.SAPLINGS, ItemTags.SAPLINGS));
        this.copyIfNeeded(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        this.copyIfNeeded(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        this.copyIfNeeded(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        this.copyIfNeeded(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        this.copyIfNeeded(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        this.copyIfNeeded(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        this.copyIfNeeded(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);

        this.copyIfNeeded(Mods.CARPENTER.blockTag("chests"), Mods.CARPENTER.itemTag("chests"));
        this.copyIfNeeded(Mods.CARPENTER.blockTag("bookshelves"), Mods.CARPENTER.itemTag("bookshelves"));

        // Fabric & Forge-specific compat
        if (Platform.isFabric()) {
            this.copyIfNeeded(Mods.FABRIC_TAGS.blockTag("chests"), Mods.FABRIC_TAGS.itemTag("chests"));
            this.copyIfNeeded(Mods.FABRIC_TAGS.blockTag("bookshelves"), Mods.FABRIC_TAGS.itemTag("bookshelves"));
        } else if (Platform.isForge()) {
            this.copyIfNeeded(Mods.FORGE.blockTag("chests/wooden"), Mods.FORGE.itemTag("chests/wooden"));
            this.copyIfNeeded(Mods.FORGE.blockTag("chests/trapped"), Mods.FORGE.itemTag("chests/trapped"));
            this.copyIfNeeded(Mods.FORGE.blockTag("bookshelves"), Mods.FORGE.itemTag("bookshelves"));
            this.tag(Mods.QUARK.itemTag("boatable_chests")).add(chest);
            this.tag(Mods.QUARK.itemTag("revertable_chests")).add(chest);
        }
    }

    protected void copyIfNeeded(TagKey<Block> blockTag, TagKey<Item> itemTag) {
        ((ItemTagsProviderAccessor) this).getTagsToCopy().putIfAbsent(blockTag, itemTag);
    }

    private static Item getAsItem(RegistryReference<Block> registryReference) {
        return registryReference.get().asItem();
    }
}

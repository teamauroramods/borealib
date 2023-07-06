package com.teamaurora.borealib.impl.datagen.util.fabric;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.base.v1.util.Mods;
import com.teamaurora.borealib.api.block.v1.compat.CommonCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibRecipeProvider;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibTagsProvider;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibTagsProvider.BorealibTagAppender;
import com.teamaurora.borealib.api.datagen.v1.providers.loot.BorealibBlockLootProvider;
import com.teamaurora.borealib.api.resource_condition.v1.DefaultResourceConditions;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
public class WoodSetGeneratorsImplImpl {

    public static void addPlatformBlockTags(BorealibTagsProvider.BlockTagProvider provider, WoodSet... woodSets) {
        BorealibTagAppender<Block> fabricChests = provider.tag(ConventionalBlockTags.CHESTS);
        BorealibTagAppender<Block> fabricBookshelves = provider.tag(ConventionalBlockTags.BOOKSHELVES);

        for (WoodSet woodSet : woodSets) {
            fabricChests.add(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST), woodSet.getBlock(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST));
            fabricBookshelves.add(woodSet.getBlock(CommonCompatBlockVariants.BOOKSHELF));
        }
    }

    public static void addPlatformItemTags(BorealibTagsProvider.ItemTagProvider provider, WoodSet... woodSets) {
        provider.copy(ConventionalBlockTags.CHESTS, ConventionalItemTags.CHESTS);
        provider.copy(ConventionalBlockTags.BOOKSHELVES, ConventionalItemTags.BOOKSHELVES);
    }

    public static void addPlatformRecipes(BorealibRecipeProvider provider, Consumer<FinishedRecipe> consumer, WoodSet... woodSets) {
        Consumer<FinishedRecipe> exporter = provider.withConditions(consumer, DefaultResourceConditions.allModsLoaded(Mods.CARPENTER));

        for (WoodSet woodSet : woodSets) {
            Block planks = woodSet.getBlock(WoodVariants.PLANKS);
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST)).define('#', planks).pattern("###").pattern("# #").pattern("###").unlockedBy("has_lots_of_items", new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.atLeast(10), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[0])).save(exporter);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, woodSet.variantOrThrow(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST).get()).requires(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST)).requires(Blocks.TRIPWIRE_HOOK).unlockedBy("has_tripwire_hook", BorealibRecipeProvider.has(Blocks.TRIPWIRE_HOOK)).save(exporter);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.BOOKSHELF).define('#', planks).define('X', Items.BOOK).pattern("###").pattern("XXX").pattern("###").unlockedBy("has_book", BorealibRecipeProvider.has(Items.BOOK)).save(exporter);
        }
    }

    public static void addPlatformBlockModels(BlockModelGenerators generators, WoodSet... woodSets) {
    }

    public static void addPlatformBlockLoot(BorealibBlockLootProvider provider, WoodSet... woodSets) {
    }
}

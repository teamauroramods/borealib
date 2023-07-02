package com.teamaurora.borealib.impl.datagen.util.forge;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.base.v1.util.Mods;
import com.teamaurora.borealib.api.block.v1.compat.CommonCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibRecipeProvider;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibTagsProvider;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibTagsProvider.BorealibTagAppender;
import com.teamaurora.borealib.api.datagen.v1.util.CompatItemTags;
import com.teamaurora.borealib.api.resource_condition.v1.DefaultResourceConditions;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
public class WoodSetGeneratorsImplImpl {
    public static void addPlatformBlockTags(BorealibTagsProvider.BlockTagProvider provider, WoodSet... woodSets) {
        BorealibTagAppender<Block> forgeWoodenChests = provider.tag(Tags.Blocks.CHESTS_WOODEN);
        BorealibTagAppender<Block> forgeTrappedChests = provider.tag(Tags.Blocks.CHESTS_TRAPPED);
        BorealibTagAppender<Block> forgeFences = provider.tag(Tags.Blocks.FENCES_WOODEN);
        BorealibTagAppender<Block> forgeFenceGates = provider.tag(Tags.Blocks.FENCE_GATES_WOODEN);
        BorealibTagAppender<Block> forgeBookshelves = provider.tag(Tags.Blocks.BOOKSHELVES);

        for (WoodSet woodSet : woodSets) {
            forgeWoodenChests.add(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST));
            forgeTrappedChests.add(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST));
            forgeFenceGates.add(woodSet.getBlock(WoodVariants.FENCE_GATE));
            forgeFences.add(woodSet.getBlock(WoodVariants.FENCE));
            forgeBookshelves.add(woodSet.getBlock(CommonCompatBlockVariants.BOOKSHELF));
        }
    }

    public static void addPlatformItemTags(BorealibTagsProvider.ItemTagProvider provider, WoodSet... woodSets) {
        provider.copy(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN);
        provider.copy(Tags.Blocks.CHESTS_TRAPPED, Tags.Items.CHESTS_TRAPPED);
        provider.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
        provider.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
        provider.copy(Tags.Blocks.BOOKSHELVES, Tags.Items.BOOKSHELVES);

        BorealibTagAppender<Item> quarkBoatableChests = provider.tag(CompatItemTags.BOATABLE_CHESTS);
        BorealibTagAppender<Item> quarkRevertableChests = provider.tag(CompatItemTags.REVERTABLE_CHESTS);

        for (WoodSet woodSet : woodSets) {
            Item chest = woodSet.getItem(CommonCompatBlockVariants.WOODEN_CHEST);
            quarkBoatableChests.add(chest);
            quarkRevertableChests.add(chest);
        }
    }

    public static void addPlatformRecipes(BorealibRecipeProvider provider, Consumer<FinishedRecipe> consumer, WoodSet... woodSets) {
        ResourceConditionProvider carpenterLoaded = DefaultResourceConditions.allModsLoaded(Mods.CARPENTER);
        Consumer<FinishedRecipe> chestExporter = provider.withConditions(consumer, DefaultResourceConditions.or(carpenterLoaded, DefaultResourceConditions.quarkFlag("variant_chests"), DefaultResourceConditions.woodworksFlag("wooden_chests")));
        Consumer<FinishedRecipe> shelfExporter = provider.withConditions(consumer, DefaultResourceConditions.or(carpenterLoaded, DefaultResourceConditions.quarkFlag("variant_bookshelves"), DefaultResourceConditions.woodworksFlag("wooden_bookshelves")));

        for (WoodSet woodSet : woodSets) {
            Block planks = woodSet.getBlock(WoodVariants.PLANKS);
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST)).define('#', planks).pattern("###").pattern("# #").pattern("###").unlockedBy("has_lots_of_items", new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.atLeast(10), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[0])).save(chestExporter);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, woodSet.variantOrThrow(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST).get()).requires(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST)).requires(Blocks.TRIPWIRE_HOOK).unlockedBy("has_tripwire_hook", BorealibRecipeProvider.has(Blocks.TRIPWIRE_HOOK)).save(chestExporter);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.BOOKSHELF).define('#', planks).define('X', Items.BOOK).pattern("###").pattern("XXX").pattern("###").unlockedBy("has_book", BorealibRecipeProvider.has(Items.BOOK)).save(shelfExporter);
        }
    }
}

package com.teamaurora.borealib.impl.datagen.providers.forge;

import com.teamaurora.borealib.api.datagen.v1.providers.BorealibRecipeProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
public class BorealibRecipeProviderImplImpl {

    public static void chestBoat(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, itemLike).requires(Tags.Items.CHESTS_WOODEN).requires(itemLike2).group("chest_boat").unlockedBy("has_boat", BorealibRecipeProvider.has(ItemTags.BOATS)).save(consumer);
    }

    public static RecipeBuilder fenceBuilder(ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike, 3).define('W', ingredient).define('#', Tags.Items.RODS_WOODEN).pattern("W#W").pattern("W#W");
    }

    public static RecipeBuilder fenceGateBuilder(ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, itemLike).define('#', Tags.Items.RODS_WOODEN).define('W', ingredient).pattern("#W#").pattern("#W#");
    }

    public static RecipeBuilder signBuilder(ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike, 3).group("sign").define('#', ingredient).define('X', Tags.Items.RODS_WOODEN).pattern("###").pattern("###").pattern(" X ");
    }

    public static void banner(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike).define('#', itemLike2).define('|', Tags.Items.RODS_WOODEN).pattern("###").pattern("###").pattern(" | ").group("banner").unlockedBy(BorealibRecipeProvider.getHasName(itemLike2), BorealibRecipeProvider.has(itemLike2)).save(consumer);
    }
}

package com.teamaurora.borealib.impl.datagen.providers;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
// Handles recipes that use Forge tags
public class BorealibRecipeProviderImpl {

    @ExpectPlatform
    public static void chestBoat(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        Platform.expect();
    }

    @ExpectPlatform
    public static RecipeBuilder fenceBuilder(ItemLike itemLike, Ingredient ingredient) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static RecipeBuilder fenceGateBuilder(ItemLike itemLike, Ingredient ingredient) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static RecipeBuilder signBuilder(ItemLike itemLike, Ingredient ingredient) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static void banner(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        Platform.expect();
    }
}

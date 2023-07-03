package com.teamaurora.borealib.api.datagen.v1.util;

import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibRecipeProvider;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibTagsProvider;
import com.teamaurora.borealib.api.datagen.v1.providers.loot.BorealibBlockLootProvider;
import com.teamaurora.borealib.impl.datagen.util.WoodSetGeneratorsImpl;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

/**
 * Contains datagen methods for generating all data for {@link WoodSet}s.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface WoodSetGenerators {

    /**
     * Generates all block tags for a mod's wood sets.
     *
     * @param provider The mod's block tag provider
     * @param woodSets The woodsets to make tags for
     */
    static void createBlockTags(BorealibTagsProvider.BlockTagProvider provider, WoodSet... woodSets) {
        WoodSetGeneratorsImpl.createBlockTags(provider, woodSets);
    }

    /**
     * Generates all item tags for a mod's wood sets.
     *
     * @param provider The mod's item tag provider
     * @param woodSets The woodsets to make tags for
     */
    static void createItemTags(BorealibTagsProvider.ItemTagProvider provider, WoodSet... woodSets) {
        WoodSetGeneratorsImpl.createItemTags(provider, woodSets);
    }

    /**
     * Generates all block states and block models for a mod's wood sets.
     *
     * @param generators The generator to add the models to
     * @param woodSets   The woodsets to make models for
     */
    static void createBlockModels(BlockModelGenerators generators, WoodSet... woodSets) {
        WoodSetGeneratorsImpl.createBlockModels(generators, woodSets);
    }

    /**
     * Generates all block tags for a mod's wood sets.
     *
     * @param generators The generator to add the models to
     * @param woodSets   The woodsets to make models for
     */
    static void createItemModels(ItemModelGenerators generators, WoodSet... woodSets) {
        WoodSetGeneratorsImpl.createItemModels(generators, woodSets);
    }

    /**
     * Generates all block loot tables for a mod's wood sets.
     *
     * @param provider The mod's block loot provider
     * @param woodSets The woodsets to make loot tables for
     */
    static void createBlockLoot(BorealibBlockLootProvider provider, WoodSet... woodSets) {
        WoodSetGeneratorsImpl.createBlockLoot(provider, woodSets);
    }

    /**
     * Generates all recipes for a mod's wood sets.
     *
     * @param provider The mod's recipe provider
     * @param consumer The consumer to save recipes to
     * @param woodSets The woodsets to make recipes for
     */
    static void createRecipes(BorealibRecipeProvider provider, Consumer<FinishedRecipe> consumer, WoodSet... woodSets) {
        WoodSetGeneratorsImpl.createRecipes(provider, consumer, woodSets);
    }
}

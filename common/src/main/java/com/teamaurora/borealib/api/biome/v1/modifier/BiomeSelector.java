package com.teamaurora.borealib.api.biome.v1.modifier;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.api.config.v1.ConfigRegistry;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeSelectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Selects what biomes a biome modifier should apply to.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface BiomeSelector extends Predicate<BiomeSelector.Context> {

    Codec<BiomeSelector> CODEC = BorealibRegistries.BIOME_SELECTOR_TYPES.byNameCodec().dispatch(BiomeSelector::type, Function.identity());

    /**
     * @return A biome selector that applies to every biome
     */
    static BiomeSelector all() {
        return BuiltInBiomeSelectors.all();
    }

    /**
     * @return A biome selector testing whether Borealib testing mode is enabled
     */
    static BiomeSelector testsEnabled() {
        return BuiltInBiomeSelectors.testsEnabled();
    }

    /**
     * Creates a biome selector wrapping an or-operation.
     *
     * @param values The selectors to test; at least 2 are required
     */
    static BiomeSelector or(BiomeSelector... values) {
        Preconditions.checkArgument(values.length >= 2, "At least 2 selectors required for an or operation");
        return BuiltInBiomeSelectors.or(Arrays.asList(values));
    }

    /**
     * Creates a biome selector wrapping an and-operation.
     *
     * @param values The selectors to test; at least 2 are required
     * @return A new and-selector
     */
    static BiomeSelector and(BiomeSelector... values) {
        Preconditions.checkArgument(values.length >= 2, "At least 2 selectors required for an and operation");
        return BuiltInBiomeSelectors.and(Arrays.asList(values));
    }

    /**
     * Creates a biome selector that returns the inverse of the given child selector.
     *
     * @param value The selector to wrap
     * @return A new not-selector
     */
    static BiomeSelector not(BiomeSelector value) {
        return BuiltInBiomeSelectors.not(value);
    }

    /**
     * Creates a biome selector testing if the biome matches any in the {@link HolderSet}.
     *
     * @param biomes The set of valid biomes
     * @return A new biome-checking selector
     */
    static BiomeSelector isBiome(HolderSet<Biome> biomes) {
        return BuiltInBiomeSelectors.biomeCheck(biomes);
    }

    /**
     * Creates a biome selector testing if the biome is found in the given dimension
     *
     * @param dimension The dimension to be checked
     * @return A new dimension-checking selector
     */
    static BiomeSelector inDimension(ResourceKey<LevelStem> dimension) {
        return BuiltInBiomeSelectors.dimensionCheck(dimension);
    }

    /**
     * @return A biome selector checking if the biome is found in the overworld
     */
    static BiomeSelector overworld() {
        return inDimension(LevelStem.OVERWORLD);
    }

    /**
     * Creates a biome selector testing if the given structure is found in the biome.
     *
     * @param structure The structure to be checked
     * @return A new structure-checking selector
     */
    static BiomeSelector hasStructure(ResourceKey<Structure> structure) {
        return BuiltInBiomeSelectors.structureCheck(structure);
    }

    /**
     * Creates a biome selector returning a boolean config value.
     *
     * @param modId The mod id of the parent config
     * @param type  The type of config to find the value in
     * @param key   The key for the config value
     * @param value The expected boolean value to return true based on
     * @return A new config toggle selector
     */
    static BiomeSelector config(String modId, ModConfig.Type type, String key, boolean value) {
        return BuiltInBiomeSelectors.configToggle(ConfigRegistry.get(modId, type).orElseThrow(), key, value);
    }

    /**
     * Creates a biome selector checking whether the biome has any of the specified features before any Borealib modifiers are applied.
     *
     * @param decoration The decoration stage to check in; all the features are expected to generate in the same step
     * @param feature    The features to check for
     * @return A new feature-checking selector
     */
    static BiomeSelector existingFeatures(GenerationStep.Decoration decoration, HolderSet<PlacedFeature> feature) {
        return BuiltInBiomeSelectors.hasExistingFeatures(decoration, feature);
    }

    /**
     * Tests the selector against the given biome.
     *
     * @param context The context to help choose whether to accept the current biome
     * @return Whether the modifier should be applied to the biome
     */
    @Override
    boolean test(Context context);

    /**
     * @return A codec to serialize and deserialize instances of this selector. It should be registered to {@link BorealibRegistries#BIOME_SELECTOR_TYPES}
     */
    Codec<? extends BiomeSelector> type();

    /**
     * Context to assist in choosing biomes.
     *
     * @since 1.0
     */
    interface Context {

        /**
         * @return Existing information for the biome that may or may not have been changed by other modifiers
         */
        BiomeInfo getExistingInfo();

        /**
         * @return The current biome
         */
        Holder<Biome> getBiome();

        /**
         * @return The key of the current biome
         */
        default ResourceKey<Biome> getBiomeKey() {
            // The biome holder should always be a reference so this is safe to do
            return this.getBiome().unwrapKey().orElseThrow();
        }

        /**
         * Checks if the specified structure can generate in the current biome.
         *
         * @param structure The structure to check
         * @return Whether the structure can generate in the biome
         */
        boolean hasStructure(ResourceKey<Structure> structure);

        /**
         * Checks if the current biome generates in the specified dimension.
         *
         * @param dimension The dimension to check
         * @return Whether the biome can spawn in the dimension
         */
        boolean generatesIn(ResourceKey<LevelStem> dimension);
    }
}

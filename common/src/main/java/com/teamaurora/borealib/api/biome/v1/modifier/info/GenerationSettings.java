package com.teamaurora.borealib.api.biome.v1.modifier.info;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

/**
 * Settings that control which features generate in what order for a given biome.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface GenerationSettings {

    /**
     * Gets the carvers for the given generation stage.
     *
     * @param carving The carving stage to get carvers for
     * @return An iterable view of carvers for that stage
     */
    Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving);

    /**
     * Gets the features for the given generation stage.
     *
     * @param decoration The decoration stage to get features for
     * @return An iterable view of features for that stage
     */
    Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration);

    /**
     * @return A list of iterable views for features ordered by their generation stages
     */
    List<Iterable<Holder<PlacedFeature>>> getFeatures();

    /**
     * Checks to see if the generation has the specified feature.
     *
     * @param decoration The decoration stage to look in
     * @param feature    The feature to look for
     * @return Whether the generation has the feature
     */
    boolean hasFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature);

    /**
     * Extends the generation settings to allow them to be modified.
     *
     * @since 1.0
     */
    interface Mutable extends GenerationSettings {

        /**
         * Adds a feature to the generation.
         *
         * @param decoration The stage to add the feature at
         * @param feature    The feature to add
         */
        Mutable addFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature);

        /**
         * Adds a carver to the generation.
         *
         * @param carving    The stage to add the carver at
         * @param feature    The carver to add
         */
        Mutable addCarver(GenerationStep.Carving carving, Holder<ConfiguredWorldCarver<?>> feature);

        /**
         * Removes a feature from the generation if it is currently present.
         *
         * @param decoration The stage to remove the feature from
         * @param feature    The key of the feature to remove
         */
        Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature);

        /**
         * Removes a feature from the generation.
         *
         * @param decoration The stage to remove the feature from
         * @param feature    The feature to remove
         */
        default Mutable removeFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature) {
            return this.removeFeature(decoration, feature.unwrapKey().orElseThrow());
        }

        /**
         * Removes a carver from the generation if it is currently present.
         *
         * @param carving The stage to remove the carver from
         * @param feature The key of the carver to remove
         */
        Mutable removeCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature);

        /**
         * Removes a carver from the generation.
         *
         * @param carving The stage to remove the carver from
         * @param feature The carver to remove
         */
        default Mutable removeCarver(GenerationStep.Carving carving, Holder<ConfiguredWorldCarver<?>> feature) {
            return this.removeCarver(carving, feature.unwrapKey().orElseThrow());
        }
    }
}
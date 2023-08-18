package com.teamaurora.borealib.api.biome.v1.modifier;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeModifierActions;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents an action a biome modifier can take on its selected biomes.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface BiomeModifierAction extends Consumer<BiomeInfo.Mutable> {

    Codec<BiomeModifierAction> CODEC = BorealibRegistries.BIOME_MODIFIER_ACTION_TYPES.byNameCodec().dispatch(BiomeModifierAction::type, Function.identity());

    /**
     * Applies the action to the given biome.
     *
     * @param info Context to modify the biome
     */
    @Override
    void accept(BiomeInfo.Mutable info);

    /**
     * @return A codec to serialize and deserialize instances of this action. It should be registered to {@link BorealibRegistries#BIOME_MODIFIER_ACTION_TYPES}
     */
    Codec<? extends BiomeModifierAction> type();

    /**
     * @return The stage to run the action at
     */
    Stage stage();


    /**
     * Creates a modifier action that adds features to the given step.
     *
     * @param decoration The decoration stage to add features to
     * @param features   The features to add
     * @return A new modification adding features at the given stage
     */
    static BiomeModifierAction addFeatures(GenerationStep.Decoration decoration, HolderSet<PlacedFeature> features) {
        return BuiltInBiomeModifierActions.addFeatures(decoration, features);
    }

    /**
     * Creates a modifier action that removes features to the given steps.
     *
     * @param decorations The decoration stages to remove features from
     * @param features    The features to remove (should be backed by an {@link BiomeSelector#existingFeatures(GenerationStep.Decoration, HolderSet) existing feature selector} if they aren't guaranteed to exist by other selectors
     * @return A new modification removing features at the given stages
     */
    static BiomeModifierAction removeFeatures(Set<GenerationStep.Decoration> decorations, HolderSet<PlacedFeature> features) {
        return BuiltInBiomeModifierActions.removeFeatures(decorations, features);
    }

    /**
     * Creates a modifier action that replaces one feature with another. The number of features will remain unchanged.
     * <p>Use {@link #replaceFeaturesNonlinear(GenerationStep.Decoration, HolderSet, HolderSet)} if there is an unknown amount of features being added and/or removed.\
     * <p>Should be backed by {@link BiomeSelector#existingFeatures(GenerationStep.Decoration, HolderSet)} if the feature existing isn't guaranteed by other selectors.
     *
     * @param decoration  The decoration stage to replace features (should be the same for both)
     * @param original    The feature to replace
     * @param replacement The feature to replace the original with
     * @return A new modification replacing features at the given stage
     */
    static BiomeModifierAction replaceFeaturesLinear(GenerationStep.Decoration decoration, Holder<PlacedFeature> original, Holder<PlacedFeature> replacement) {
        return BuiltInBiomeModifierActions.replaceFeaturesLinear(decoration, original, replacement);
    }

    /**
     * Creates a modifier action replacing one group of features with another. There may be more or less total features afterward.
     * <p>Should be backed by {@link BiomeSelector#existingFeatures(GenerationStep.Decoration, HolderSet)} if the feature existing isn't guaranteed by other selectors.
     *
     * @param decoration   The decoration stage to replace features (should be the same for all)
     * @param originals    The features to replace
     * @param replacements The features to replace the originals with
     * @return A new modification replacing features at the given stage
     */
    static BiomeModifierAction replaceFeaturesNonlinear(GenerationStep.Decoration decoration, HolderSet<PlacedFeature> originals, HolderSet<PlacedFeature> replacements) {
        return BuiltInBiomeModifierActions.replaceFeaturesNonlinear(decoration, originals, replacements);
    }

    /**
     * Creates a modifier action adding a spawn to the biome.
     *
     * @param datum The spawner to add to the biome
     * @return A new action adding the spawn to the biome
     */
    static BiomeModifierAction addSpawn(MobSpawnSettings.SpawnerData datum) {
        return addSpawns(Collections.singletonList(datum));
    }

    /**
     * Creates a modifier action adding spawns to the biome.
     *
     * @param data The spawners to add to the biome
     * @return A new action adding the spawns to the biome
     */
    static BiomeModifierAction addSpawns(List<MobSpawnSettings.SpawnerData> data) {
        return BuiltInBiomeModifierActions.addSpawns(data);
    }

    /**
     * Creates a modifier action removing spawns from the biome.
     *
     * @param entityTypes The entities to remove spawns for
     * @return A new action removing spawns
     */
    static BiomeModifierAction removeSpawns(HolderSet<EntityType<?>> entityTypes) {
        return BuiltInBiomeModifierActions.removeSpawns(entityTypes);
    }

    /**
     * Creates a modifier action replacing one entity spawn in the biome with another. All other data such as counts and costs will remain untouched.
     * <p>Should be backed by {@link BiomeSelector#existingSpawn(MobCategory, EntityType)} if the spawn isn't guaranteed to exist by other selectors.
     *
     * @param original    The original entity
     * @param replacement The entity to replace the original with
     * @return A new modifier action replacing the first entity with the second
     */
    static BiomeModifierAction replaceSpawn(EntityType<?> original, EntityType<?> replacement) {
        return BuiltInBiomeModifierActions.replaceSpawn(original, replacement);
    }

    /**
     * Creates a modifier action that sets the biome fog color.
     *
     * @param color The color to change the fog to
     * @return A new fog color modification
     */
    static BiomeModifierAction setFogColor(int color) {
        return BuiltInBiomeModifierActions.setFogColor(color);
    }

    /**
     * Creates a modifier action that sets the biome water color.
     *
     * @param color The color to change the water to
     * @return A new water color modification
     */
    static BiomeModifierAction setWaterColor(int color) {
        return BuiltInBiomeModifierActions.setWaterColor(color);
    }

    /**
     * Creates a modifier action that sets the biome water fog color.
     *
     * @param color The color to change the water fog to
     * @return A new water fog color modification
     */
    static BiomeModifierAction setWaterFogColor(int color) {
        return BuiltInBiomeModifierActions.setWaterFogColor(color);
    }

    /**
     * Creates a modifier action that sets the biome sky color.
     *
     * @param color The color to change the sky to
     * @return A new sky color modification
     */
    static BiomeModifierAction setSkyColor(int color) {
        return BuiltInBiomeModifierActions.setSkyColor(color);
    }

    /**
     * Creates a modifier action that sets the biome foliage color.
     *
     * @param color The color to change the foliage to
     * @return A new foliage color modification
     */
    static BiomeModifierAction setFoliageColor(int color) {
        return BuiltInBiomeModifierActions.setFoliageColor(color);
    }

    /**
     * Creates a modifier action that sets the biome grass color.
     *
     * @param color The color to change the grass to
     * @return A new grass color modification
     */
    static BiomeModifierAction setGrassColor(int color) {
        return BuiltInBiomeModifierActions.setGrassColor(color);
    }

    /**
     * Creates a modifier action that sets the biome grass color modifier.
     *
     * @param modifier The new grass color modifier
     * @return A new action that alters the grass color modifier
     */
    static BiomeModifierAction setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier modifier) {
        return BuiltInBiomeModifierActions.setGrassColorModifier(modifier);
    }

    /**
     * Creates a modifier action that sets the biome ambient particle settings.
     *
     * @param settings The new ambient particle settings
     * @return A new action that alters the ambient particle settings
     */
    static BiomeModifierAction setAmbientParticle(AmbientParticleSettings settings) {
        return BuiltInBiomeModifierActions.setAmbientParticle(settings);
    }

    /**
     * Creates a modifier action that sets the biome ambient sound.
     *
     * @param sound The new ambient sound
     * @return A new action that alters the ambient sound
     */
    static BiomeModifierAction setAmbientSound(Holder<SoundEvent> sound) {
        return BuiltInBiomeModifierActions.setAmbientSound(sound);
    }

    /**
     * Creates a modifier action that sets the biome mood settings.
     *
     * @param settings The new mood settings
     * @return A new action that alters the mood settings
     */
    static BiomeModifierAction setMoodSound(AmbientMoodSettings settings) {
        return BuiltInBiomeModifierActions.setMoodSound(settings);
    }

    /**
     * Creates a modifier action that sets the biome ambient additions sound.
     *
     * @param settings The new ambient additions sound settings
     * @return A new action that alters the ambient additions sound
     */
    static BiomeModifierAction setAdditionsSound(AmbientAdditionsSettings settings) {
        return BuiltInBiomeModifierActions.setAdditionsSound(settings);
    }

    /**
     * Creates a modifier action that sets the biome music.
     *
     * @param music The new music
     * @return A new action that alters the music
     */
    static BiomeModifierAction setMusic(Music music) {
        return BuiltInBiomeModifierActions.setMusic(music);
    }

    /**
     * Stages of biome modification.
     *
     * @since 1.0
     */
    enum Stage {

        /**
         * Used for adding properties; this runs first.
         */
        ADDITIONS,

        /**
         * Used for removing properties from biomes.
         */
        REMOVALS,

        /**
         * Used for combinations of additions and removals to replace properties. The amount of additions should be equal to that of the removals.
         */
        REPLACEMENTS,

        /**
         * Used for wide-reaching post-processing of biome properties; this runs last.
         */
        POST_PROCESSING
    }
}

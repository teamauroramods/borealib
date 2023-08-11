package com.teamaurora.borealib.api.biome.v1.modifier;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeModifierActions;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

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


    // Effects Modifications \\

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

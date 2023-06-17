package com.teamaurora.borealib.api.biome.v1.modifier.info;

import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Controls biome special effects, such as music or environmental appearance.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface SpecialEffectSettings {

    /**
     * @return The fog color of the biome
     */
    int getFogColor();

    /**
     * @return The water color of the biome
     */
    int getWaterColor();

    /**
     * @return The water fog color of the biome
     */
    int getWaterFogColor();

    /**
     * @return The sky color of the biome
     */
    int getSkyColor();

    /**
     * @return The foliage color override of the biome if it exists, otherwise {@link OptionalInt#empty()}
     */
    OptionalInt getFoliageColorOverride();

    /**
     * @return The grass color override of the biome if it exists, otherwise {@link OptionalInt#empty()}
     */
    OptionalInt getGrassColorOverride();

    /**
     * @return The grass color modifier for the biome
     */
    GrassColorModifier getGrassColorModifier();

    /**
     * @return The ambient particle settings of the biome if they exist, otherwise {@link Optional#empty()}
     */
    Optional<AmbientParticleSettings> getAmbientParticle();

    /**
     * @return An ambient looping sound event played in the biome if it exists, otherwise {@link Optional#empty()}
     */
    Optional<Holder<SoundEvent>> getAmbientLoopSound();

    /**
     * @return The ambient mood settings of the biome if they exist, otherwise {@link Optional#empty()}
     */
    Optional<AmbientMoodSettings> getAmbientMoodSound();

    /**
     * @return The ambient addition settings of the biome if they exist, otherwise {@link Optional#empty()}
     */
    Optional<AmbientAdditionsSettings> getAmbientAdditionsSound();

    /**
     * @return The biome's background music if it exists, otherwise {@link Optional#empty()}
     */
    Optional<Music> getBackgroundMusic();

    /**
     * Extends the special effect settings to allow them to be modified.
     *
     * @since 1.0
     */
    interface Mutable extends SpecialEffectSettings {

        /**
         * Sets a new fog color for the biome.
         *
         * @param color The new fog color
         */
        SpecialEffectSettings.Mutable setFogColor(int color);

        /**
         * Sets a new water color for the biome.
         *
         * @param color The new water color
         */
        SpecialEffectSettings.Mutable setWaterColor(int color);

        /**
         * Sets a new water fog color for the biome.
         *
         * @param color The new water fog color
         */
        SpecialEffectSettings.Mutable setWaterFogColor(int color);

        /**
         * Sets a new sky color for the biome.
         *
         * @param color The new sky color
         */
        SpecialEffectSettings.Mutable setSkyColor(int color);

        /**
         * Sets a new foliage color override for the biome.
         *
         * @param colorOverride The new foliage color override
         */
        SpecialEffectSettings.Mutable setFoliageColorOverride(@Nullable Integer colorOverride);

        /**
         * Sets a new grass color override for the biome.
         *
         * @param colorOverride The new grass color override
         */
        SpecialEffectSettings.Mutable setGrassColorOverride(@Nullable Integer colorOverride);

        /**
         * Sets a new grass color modifier for the biome.
         *
         * @param modifier The new grass color modifier
         */
        SpecialEffectSettings.Mutable setGrassColorModifier(GrassColorModifier modifier);

        /**
         * Sets new ambient particle settings for the biome.
         *
         * @param settings The new ambient particle settings
         */
        SpecialEffectSettings.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings);

        /**
         * Sets a new ambient loop sound for the biome.
         *
         * @param sound The new ambient loop sound
         */
        SpecialEffectSettings.Mutable setAmbientLoopSound(@Nullable Holder<SoundEvent> sound);

        /**
         * Sets new ambient mood settings for the biome.
         *
         * @param settings The new ambient mood settings
         */
        SpecialEffectSettings.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings);

        /**
         * Sets new ambient addition settings for the biome.
         *
         * @param settings The new ambient addition settings
         */
        SpecialEffectSettings.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings);

        /**
         * Sets new background music for the biome.
         *
         * @param music The new background music
         */
        SpecialEffectSettings.Mutable setBackgroundMusic(@Nullable Music music);
    }
}
package com.teamaurora.borealib.api.biome.v1.modifier.info;

/**
 * Wraps platform-specific information about a biome into an accessible format.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface BiomeInfo {

    /**
     * @return The climate settings of the biome
     */
    ClimateSettings getClimateSettings();

    /**
     * @return The special effect settings of the biome
     */
    SpecialEffectSettings getEffectSettings();

    /**
     * @return The generation settings of the biome
     */
    GenerationSettings getGenerationSettings();

    /**
     * @return The biome's mob spawn settings
     */
    SpawnSettings getSpawnSettings();

    /**
     * Allows for biome information to be modified in addition to being queried.
     *
     * @since 1.0
     */
    interface Mutable extends BiomeInfo {

        @Override
        ClimateSettings.Mutable getClimateSettings();
        
        @Override
        SpecialEffectSettings.Mutable getEffectSettings();
        
        @Override
        GenerationSettings.Mutable getGenerationSettings();
        
        @Override
        SpawnSettings.Mutable getSpawnSettings();
    }
}
package dev.tesseract.biomes.impl;

import dev.tesseract.biomes.*;
import net.minecraft.world.level.biome.Biome;

public class MutableBiomeProperties extends ImmutableBiomeProperties implements BiomeProperties.Mutable {

    public MutableBiomeProperties(Biome biome, ClimateSettings.Mutable climateSettings, SpecialEffectSettings.Mutable specialEffectSettings, GenerationSettings.Mutable generationSettings, SpawnSettings.Mutable spawnSettings) {
        super(biome, climateSettings, specialEffectSettings, generationSettings, spawnSettings);
    }

    public MutableBiomeProperties(Biome biome) {
        super(biome);
    }

    @Override
    public ClimateSettings.Mutable getClimateProperties() {
        return (ClimateSettings.Mutable) super.getClimateProperties();
    }

    @Override
    public SpecialEffectSettings.Mutable getEffectsProperties() {
        return (SpecialEffectSettings.Mutable) super.getEffectsProperties();
    }

    @Override
    public GenerationSettings.Mutable getGenerationProperties() {
        return (GenerationSettings.Mutable) super.getGenerationProperties();
    }

    @Override
    public SpawnSettings.Mutable getSpawnProperties() {
        return (SpawnSettings.Mutable) super.getSpawnProperties();
    }
}

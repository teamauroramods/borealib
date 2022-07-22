package dev.tesseract.biomes.impl;

import dev.tesseract.biomes.BiomeHelper;
import dev.tesseract.biomes.ClimateSettings;
import net.minecraft.world.level.biome.Biome;

public class ImmutableClimateSettings implements ClimateSettings {

    protected final Biome.ClimateSettings climateSettings;

    public ImmutableClimateSettings(Biome biome) {
        this(BiomeHelper.getClimateSettings(biome));
    }

    public ImmutableClimateSettings(Biome.ClimateSettings climateSettings) {
        this.climateSettings = climateSettings;
    }

    @Override
    public Biome.Precipitation getPrecipitation() {
        return climateSettings.precipitation;
    }

    @Override
    public float getTemperature() {
        return climateSettings.temperature;
    }

    @Override
    public Biome.TemperatureModifier getTemperatureModifier() {
        return climateSettings.temperatureModifier;
    }

    @Override
    public float getDownfall() {
        return climateSettings.downfall;
    }
}

package dev.tesseract.biomes.fabric;

import dev.tesseract.biomes.ClimateSettings;
import dev.tesseract.biomes.impl.ImmutableClimateSettings;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.minecraft.world.level.biome.Biome;

public class MutableClimateSettingsImpl extends ImmutableClimateSettings implements ClimateSettings.Mutable {

    private final BiomeModificationContext.WeatherContext parent;

    public MutableClimateSettingsImpl(Biome biome, BiomeModificationContext.WeatherContext parent) {
        super(biome);
        this.parent = parent;
    }

    @Override
    public ClimateSettings.Mutable setPrecipitation(Biome.Precipitation precipitation) {
        this.parent.setPrecipitation(precipitation);
        return this;
    }

    @Override
    public ClimateSettings.Mutable setTemperature(float temperature) {
        this.parent.setTemperature(temperature);
        return this;
    }

    @Override
    public ClimateSettings.Mutable setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
        this.parent.setTemperatureModifier(temperatureModifier);
        return this;
    }

    @Override
    public ClimateSettings.Mutable setDownfall(float downfall) {
        this.parent.setDownfall(downfall);
        return this;
    }
}

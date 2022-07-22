package dev.tesseract.biomes.forge;

import dev.tesseract.biomes.ClimateSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.ClimateSettingsBuilder;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ClimateSettingsImpl implements ClimateSettings {

    protected final ClimateSettingsBuilder parent;

    public ClimateSettingsImpl(ClimateSettingsBuilder parent) {
        this.parent = parent;
    }

    @Override
    public Biome.Precipitation getPrecipitation() {
        return this.parent.getPrecipitation();
    }

    @Override
    public float getTemperature() {
        return this.parent.getTemperature();
    }

    @Override
    public Biome.TemperatureModifier getTemperatureModifier() {
        return this.parent.getTemperatureModifier();
    }

    @Override
    public float getDownfall() {
        return this.parent.getDownfall();
    }

    public static class Mutable extends ClimateSettingsImpl implements ClimateSettings.Mutable {

        public Mutable(ClimateSettingsBuilder parent) {
            super(parent);
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
}

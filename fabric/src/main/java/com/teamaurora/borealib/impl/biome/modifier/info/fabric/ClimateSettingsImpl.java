package com.teamaurora.borealib.impl.biome.modifier.info.fabric;

import com.teamaurora.borealib.api.biome.v1.modifier.info.ClimateSettings;
import com.teamaurora.borealib.core.mixin.fabric.BiomeAccessor;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ClimateSettingsImpl implements ClimateSettings {

    private final Biome.ClimateSettings climateSettings;

    ClimateSettingsImpl(Biome biome) {
        this.climateSettings = ((BiomeAccessor) (Object) biome).getClimateSettings();
    }

    @Override
    public boolean hasPrecipitation() {
        return this.climateSettings.hasPrecipitation();
    }

    @Override
    public float getTemperature() {
        return this.climateSettings.temperature();
    }

    @Override
    public Biome.TemperatureModifier getTemperatureModifier() {
        return this.climateSettings.temperatureModifier();
    }

    @Override
    public float getDownfall() {
        return this.climateSettings.downfall();
    }

    public static class Mutable extends ClimateSettingsImpl implements ClimateSettings.Mutable {

        private final BiomeModificationContext.WeatherContext context;

        Mutable(Biome biome, BiomeModificationContext.WeatherContext context) {
            super(biome);
            this.context = context;
        }

        @Override
        public ClimateSettings.Mutable setHasPrecipitation(boolean hasPrecipitation) {
            this.context.setPrecipitation(hasPrecipitation);
            return this;
        }

        @Override
        public ClimateSettings.Mutable setTemperature(float temperature) {
            this.context.setTemperature(temperature);
            return this;
        }

        @Override
        public ClimateSettings.Mutable setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
            this.context.setTemperatureModifier(temperatureModifier);
            return this;
        }

        @Override
        public ClimateSettings.Mutable setDownfall(float downfall) {
            this.context.setDownfall(downfall);
            return this;
        }
    }
}

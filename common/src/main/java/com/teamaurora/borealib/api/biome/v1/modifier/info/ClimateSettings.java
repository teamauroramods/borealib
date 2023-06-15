package com.teamaurora.borealib.api.biome.v1.modifier.info;

import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;

public interface ClimateSettings {

    boolean hasPrecipitation();

    float getTemperature();

    TemperatureModifier getTemperatureModifier();

    float getDownfall();

    interface Mutable extends ClimateSettings {
        Mutable setHasPrecipitation(boolean hasPrecipitation);

        Mutable setTemperature(float temperature);

        Mutable setTemperatureModifier(TemperatureModifier temperatureModifier);

        Mutable setDownfall(float downfall);
    }
}
package com.teamaurora.borealib.api.biome.v1.modifier.info;

import net.minecraft.world.level.biome.Biome.TemperatureModifier;

/**
 * Climate data for a given biome.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface ClimateSettings {

    /**
     * @return Whether precipitation can fall in the biome
     */
    boolean hasPrecipitation();

    /**
     * @return The temperature of the biome
     */
    float getTemperature();

    /**
     * @return The temperature modifier for the biome
     */
    TemperatureModifier getTemperatureModifier();

    /**
     * @return The downfall value of the biome.
     */
    float getDownfall();

    /**
     * Extends the climate settings to allow them to be modified.
     *
     * @since 1.0
     */
    interface Mutable extends ClimateSettings {

        /**
         * Sets whether the biome has precipitation.
         *
         * @param hasPrecipitation The new precipitation toggle
         */
        Mutable setHasPrecipitation(boolean hasPrecipitation);

        /**
         * Sets a new temperature for the biome.
         *
         * @param temperature The new temperature value
         */
        Mutable setTemperature(float temperature);

        /**
         * Sets a new temperature modifier for the biome.
         *
         * @param temperatureModifier The new temperature modifier
         */
        Mutable setTemperatureModifier(TemperatureModifier temperatureModifier);

        /**
         * Sets a new downfall amount for the biome.
         *
         * @param downfall The new downfall value
         */
        Mutable setDownfall(float downfall);
    }
}
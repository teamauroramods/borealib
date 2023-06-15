package com.teamaurora.borealib.impl.biome.modifier.info.forge;

import com.teamaurora.borealib.api.biome.v1.modifier.info.ClimateSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.ClimateSettingsBuilder;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ClimateSettingsImpl implements ClimateSettings.Mutable {

    private final ClimateSettingsBuilder builder;

    ClimateSettingsImpl(ClimateSettingsBuilder builder) {
        this.builder = builder;
    }

    @Override
    public boolean hasPrecipitation() {
        return this.builder.hasPrecipitation();
    }

    @Override
    public float getTemperature() {
        return this.builder.getTemperature();
    }

    @Override
    public Biome.TemperatureModifier getTemperatureModifier() {
        return this.builder.getTemperatureModifier();
    }

    @Override
    public float getDownfall() {
        return this.builder.getDownfall();
    }

    @Override
    public Mutable setHasPrecipitation(boolean hasPrecipitation) {
        this.builder.setHasPrecipitation(hasPrecipitation);
        return this;
    }

    @Override
    public Mutable setTemperature(float temperature) {
        this.builder.setTemperature(temperature);
        return this;
    }

    @Override
    public Mutable setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
        this.builder.setTemperatureModifier(temperatureModifier);
        return this;
    }

    @Override
    public Mutable setDownfall(float downfall) {
        this.builder.setDownfall(downfall);
        return this;
    }
}

package com.teamaurora.borealib.impl.biome.modifier.info.fabric;

import com.teamaurora.borealib.api.biome.v1.modifier.info.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.world.level.biome.Biome;

public class BiomeInfoImpl implements BiomeInfo {

    private final ClimateSettingsImpl climateSettings;
    private final GenerationSettingsImpl generationSettings;
    private final SpecialEffectSettingsImpl specialEffectSettings;
    private final SpawnSettingsImpl spawnSettings;

    private BiomeInfoImpl(BiomeSelectionContext context) {
        this(
                new ClimateSettingsImpl(context.getBiome()),
                new GenerationSettingsImpl(context),
                new SpecialEffectSettingsImpl(context.getBiome()),
                new SpawnSettingsImpl(context.getBiome())
        );
    }

    public static BiomeInfoImpl wrap(BiomeSelectionContext context) {
        return new BiomeInfoImpl(context);
    }

    protected BiomeInfoImpl(ClimateSettingsImpl climateSettings, GenerationSettingsImpl generationSettings, SpecialEffectSettingsImpl specialEffectSettings, SpawnSettingsImpl spawnSettings) {
        this.climateSettings = climateSettings;
        this.generationSettings = generationSettings;
        this.specialEffectSettings = specialEffectSettings;
        this.spawnSettings = spawnSettings;
    }

    @Override
    public ClimateSettings getClimateSettings() {
        return this.climateSettings;
    }

    @Override
    public SpecialEffectSettings getEffectSettings() {
        return this.specialEffectSettings;
    }

    @Override
    public GenerationSettings getGenerationSettings() {
        return this.generationSettings;
    }

    @Override
    public SpawnSettings getSpawnSettings() {
        return null;
    }

    public static class Mutable extends BiomeInfoImpl implements BiomeInfo.Mutable {

        public Mutable(Biome biome, BiomeModificationContext context, BiomeSelectionContext selectionContext) {
            super(
                    new ClimateSettingsImpl.Mutable(biome, context.getWeather()),
                    new GenerationSettingsImpl.Mutable(context.getGenerationSettings(), selectionContext),
                    new SpecialEffectSettingsImpl.Mutable(biome, context.getEffects()),
                    new SpawnSettingsImpl.Mutable(biome, context.getSpawnSettings())
            );
        }

        @Override
        public ClimateSettings.Mutable getClimateSettings() {
            return (ClimateSettings.Mutable) super.getClimateSettings();
        }

        @Override
        public SpecialEffectSettings.Mutable getEffectSettings() {
            return (SpecialEffectSettings.Mutable) super.getEffectSettings();
        }

        @Override
        public GenerationSettings.Mutable getGenerationSettings() {
            return (GenerationSettings.Mutable) super.getGenerationSettings();
        }

        @Override
        public SpawnSettings.Mutable getSpawnSettings() {
            return (SpawnSettings.Mutable) super.getSpawnSettings();
        }
    }
}

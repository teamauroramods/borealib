package dev.tesseract.biomes.forge;

import dev.tesseract.biomes.*;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class BiomePropertiesImpl implements BiomeProperties {

    protected final ClimateSettings climateSettings;
    protected final SpecialEffectSettings specialEffectSettings;
    protected final GenerationSettings generationSettings;
    protected final SpawnSettings spawnSettings;

    public BiomePropertiesImpl(ModifiableBiomeInfo.BiomeInfo.Builder parent) {
        this(new ClimateSettingsImpl(parent.getClimateSettings()),
                new SpecialEffectSettingsImpl(parent.getSpecialEffects()),
                new GenerationSettingsImpl(parent.getGenerationSettings()),
                new SpawnSettingsImpl(parent.getMobSpawnSettings())
        );
    }

    public BiomePropertiesImpl(ClimateSettings climateSettings, SpecialEffectSettings specialEffectSettings, GenerationSettings generationSettings, SpawnSettings spawnSettings) {
        this.climateSettings = climateSettings;
        this.specialEffectSettings = specialEffectSettings;
        this.generationSettings = generationSettings;
        this.spawnSettings = spawnSettings;
    }

    @Override
    public ClimateSettings getClimateSettings() {
        return this.climateSettings;
    }

    @Override
    public SpecialEffectSettings getSpecialEffectSettings() {
        return this.specialEffectSettings;
    }

    @Override
    public GenerationSettings getGenerationSettings() {
        return this.generationSettings;
    }

    @Override
    public SpawnSettings getSpawnSettings() {
        return this.spawnSettings;
    }
}

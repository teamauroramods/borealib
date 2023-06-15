package com.teamaurora.borealib.impl.biome.modifier.info.forge;

import com.teamaurora.borealib.api.biome.v1.modifier.info.*;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BiomeInfoImpl implements BiomeInfo.Mutable {

    private final ClimateSettingsImpl climateSettings;
    private final GenerationSettingsImpl generationSettings;
    private final SpecialEffectSettingsImpl specialEffectSettings;
    private final SpawnSettingsImpl spawnSettings;

    public BiomeInfoImpl(ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        this.climateSettings = new ClimateSettingsImpl(builder.getClimateSettings());
        this.generationSettings = new GenerationSettingsImpl(builder.getGenerationSettings());
        this.specialEffectSettings = new SpecialEffectSettingsImpl(builder.getSpecialEffects());
        this.spawnSettings = new SpawnSettingsImpl(builder.getMobSpawnSettings());
    }

    @Override
    public ClimateSettings.Mutable getClimateSettings() {
        return this.climateSettings;
    }

    @Override
    public SpecialEffectSettings.Mutable getEffectSettings() {
        return this.specialEffectSettings;
    }

    @Override
    public GenerationSettings.Mutable getGenerationSettings() {
        return this.generationSettings;
    }

    @Override
    public SpawnSettings.Mutable getSpawnSettings() {
        return this.spawnSettings;
    }
}

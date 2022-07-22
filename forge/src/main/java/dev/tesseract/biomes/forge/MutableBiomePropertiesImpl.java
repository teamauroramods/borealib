package dev.tesseract.biomes.forge;

import dev.tesseract.biomes.*;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class MutableBiomePropertiesImpl extends BiomePropertiesImpl implements BiomeProperties.Mutable {

    public MutableBiomePropertiesImpl(ModifiableBiomeInfo.BiomeInfo.Builder parent) {
        super(new ClimateSettingsImpl.Mutable(parent.getClimateSettings()),
                new SpecialEffectSettingsImpl.Mutable(parent.getSpecialEffects()),
                new GenerationSettingsImpl.Mutable(parent.getGenerationSettings()),
                new SpawnSettingsImpl.Mutable(parent.getMobSpawnSettings())
        );
    }

    @Override
    public ClimateSettings.Mutable getClimateSettings() {
        return (ClimateSettings.Mutable) super.getClimateSettings();
    }

    @Override
    public SpecialEffectSettings.Mutable getSpecialEffectSettings() {
        return (SpecialEffectSettings.Mutable) super.getSpecialEffectSettings();
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

package dev.tesseract.biomes.fabric;

import dev.tesseract.biomes.BiomeProperties;
import dev.tesseract.biomes.impl.MutableBiomeProperties;
import dev.tesseract.core.mixin.fabric.BiomeAccessor;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.minecraft.world.level.biome.Biome;

public class BiomeHelperImpl {

    public static Biome.ClimateSettings getClimateSettings(Biome biome) {
        return ((BiomeAccessor) (Object) biome).getClimateSettings();
    }

    public static BiomeProperties.Mutable newMutableProperties(Biome biome, BiomeModificationContext parent) {
        return new MutableBiomeProperties(biome,
                new MutableClimateSettingsImpl(biome, parent.getWeather()),
                new MutableSpecialEffectSettingsImpl(biome, parent.getEffects()),
                new MutableGenerationSettingsImpl(biome, parent.getGenerationSettings()),
                new MutableSpawnSettingsImpl(biome, parent.getSpawnSettings()));
    }
}

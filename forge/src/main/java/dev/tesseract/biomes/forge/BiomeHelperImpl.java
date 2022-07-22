package dev.tesseract.biomes.forge;

import net.minecraft.world.level.biome.Biome;

public class BiomeHelperImpl {
    public static Biome.ClimateSettings getClimateSettings(Biome biome) {
        return biome.getModifiedClimateSettings();
    }
}

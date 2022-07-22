package dev.tesseract.biomes.impl;

import dev.tesseract.biomes.GenerationSettings;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Collections;
import java.util.List;

public class ImmutableGenerationSettings implements GenerationSettings {
    protected final BiomeGenerationSettings settings;

    public ImmutableGenerationSettings(Biome biome) {
        this(biome.getGenerationSettings());
    }

    public ImmutableGenerationSettings(BiomeGenerationSettings settings) {
        this.settings = settings;
    }

    @Override
    public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving) {
        return settings.getCarvers(carving);
    }

    @Override
    public Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration) {
        if (decoration.ordinal() >= settings.features().size()) {
            return Collections.emptyList();
        }
        return settings.features().get(decoration.ordinal());
    }

    @Override
    public List<Iterable<Holder<PlacedFeature>>> getFeatures() {
        return (List<Iterable<Holder<PlacedFeature>>>) (List<?>) settings.features();
    }
}

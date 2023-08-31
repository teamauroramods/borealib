package com.teamaurora.borealib.impl.biome.modifier.info.fabric;

import com.teamaurora.borealib.api.biome.v1.modifier.info.GenerationSettings;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.List;

@ApiStatus.Internal
public class GenerationSettingsImpl implements GenerationSettings {

    private final BiomeGenerationSettings settings;
    private final BiomeSelectionContext context;

    GenerationSettingsImpl(BiomeSelectionContext context) {
        this.settings = context.getBiome().getGenerationSettings();
        this.context = context;
    }

    @Override
    public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving) {
        return this.settings.getCarvers(carving);
    }

    @Override
    public Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration) {
        if (decoration.ordinal() >= settings.features().size())
            return Collections.emptyList();
        return settings.features().get(decoration.ordinal());
    }

    @Override
    public List<Iterable<Holder<PlacedFeature>>> getFeatures() {
        return (List<Iterable<Holder<PlacedFeature>>>) (List<?>) this.settings.features();
    }

    @Override
    public boolean hasFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature) {
        return this.context.hasPlacedFeature(feature.unwrapKey().orElseThrow());
    }

    public static class Mutable extends GenerationSettingsImpl implements GenerationSettings.Mutable {

        private final BiomeModificationContext.GenerationSettingsContext context;

        Mutable(BiomeModificationContext.GenerationSettingsContext context, BiomeSelectionContext selectionContext) {
            super(selectionContext);
            this.context = context;
        }

        @Override
        public GenerationSettings.Mutable addFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature) {
            this.context.addFeature(decoration, feature.unwrapKey().orElseThrow());
            return this;
        }

        @Override
        public GenerationSettings.Mutable addCarver(GenerationStep.Carving carving, Holder<ConfiguredWorldCarver<?>> feature) {
            this.context.addCarver(carving, feature.unwrapKey().orElseThrow());
            return this;
        }

        @Override
        public GenerationSettings.Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature) {
            this.context.removeFeature(decoration, feature);
            return this;
        }

        @Override
        public GenerationSettings.Mutable removeCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature) {
            this.context.removeCarver(carving, feature);
            return this;
        }
    }
}

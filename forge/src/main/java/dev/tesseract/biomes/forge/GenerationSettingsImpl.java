package dev.tesseract.biomes.forge;

import dev.tesseract.biomes.GenerationSettings;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;

import java.util.List;

public class GenerationSettingsImpl implements GenerationSettings {

    protected final BiomeGenerationSettingsBuilder parent;

    public GenerationSettingsImpl(BiomeGenerationSettingsBuilder parent) {
        this.parent = parent;
    }

    @Override
    public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving) {
        return this.parent.getCarvers(carving);
    }

    @Override
    public Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration) {
        return this.parent.getFeatures(decoration);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Iterable<Holder<PlacedFeature>>> getFeatures() {
        return (List<Iterable<Holder<PlacedFeature>>>) (List<?>) this.parent.features;
    }

    public static class Mutable extends GenerationSettingsImpl implements GenerationSettings.Mutable {

        public Mutable(BiomeGenerationSettingsBuilder parent) {
            super(parent);
        }

        @Override
        public GenerationSettings.Mutable addFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature) {
            this.parent.addFeature(decoration, feature);
            return this;
        }

        @Override
        public GenerationSettings.Mutable addCarver(GenerationStep.Carving carving, Holder<ConfiguredWorldCarver<?>> feature) {
            this.parent.addCarver(carving, feature);
            return this;
        }

        @Override
        public GenerationSettings.Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature) {
            this.parent.getFeatures(decoration).removeIf(holder -> holder.is(feature));
            return this;
        }

        @Override
        public GenerationSettings.Mutable removeCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature) {
            this.parent.getCarvers(carving).removeIf(holder -> holder.is(feature));
            return this;
        }
    }
}

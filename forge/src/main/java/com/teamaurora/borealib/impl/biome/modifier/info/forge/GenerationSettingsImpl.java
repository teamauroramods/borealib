package com.teamaurora.borealib.impl.biome.modifier.info.forge;

import com.teamaurora.borealib.api.biome.v1.modifier.info.GenerationSettings;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class GenerationSettingsImpl implements GenerationSettings.Mutable {

    private final BiomeGenerationSettingsBuilder builder;

    GenerationSettingsImpl(BiomeGenerationSettingsBuilder builder) {
        this.builder = builder;
    }

    @Override
    public List<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving) {
        return this.builder.getCarvers(carving);
    }

    @Override
    public List<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration) {
        return this.builder.getFeatures(decoration);
    }

    @Override
    public List<Iterable<Holder<PlacedFeature>>> getFeatures() {
        return (List<Iterable<Holder<PlacedFeature>>>) (List<?>) this.builder.features;
    }

    @Override
    public boolean hasFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature) {
        return this.getFeatures(decoration).contains(feature);
    }

    @Override
    public Mutable addFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature) {
        this.builder.addFeature(decoration, feature);
        return this;
    }

    @Override
    public Mutable addCarver(GenerationStep.Carving carving, Holder<ConfiguredWorldCarver<?>> feature) {
        this.builder.addCarver(carving, feature);
        return this;
    }

    @Override
    public Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature) {
        this.getFeatures(decoration).removeIf(holder -> holder.is(feature));
        return this;
    }

    @Override
    public Mutable removeCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature) {
        this.getCarvers(carving).removeIf(holder -> holder.is(feature));
        return this;
    }
}

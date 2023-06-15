package com.teamaurora.borealib.api.biome.v1.modifier.info;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public interface GenerationSettings {

    Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving);
    
    Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration);
    
    List<Iterable<Holder<PlacedFeature>>> getFeatures();

    boolean hasFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature);
    
    interface Mutable extends GenerationSettings {

        Mutable addFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature);
        
        Mutable addCarver(GenerationStep.Carving carving, Holder<ConfiguredWorldCarver<?>> feature);
        
        Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature);
        
        Mutable removeCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature);
    }
}
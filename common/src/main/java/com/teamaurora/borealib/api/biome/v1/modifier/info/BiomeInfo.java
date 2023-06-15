package com.teamaurora.borealib.api.biome.v1.modifier.info;

public interface BiomeInfo {

    ClimateSettings getClimateSettings();
    
    SpecialEffectSettings getEffectSettings();
    
    GenerationSettings getGenerationSettings();
    
    SpawnSettings getSpawnSettings();
    
    interface Mutable extends BiomeInfo {

        @Override
        ClimateSettings.Mutable getClimateSettings();
        
        @Override
        SpecialEffectSettings.Mutable getEffectSettings();
        
        @Override
        GenerationSettings.Mutable getGenerationSettings();
        
        @Override
        SpawnSettings.Mutable getSpawnSettings();
    }
}
package com.teamaurora.borealib.api.biome.v1.modifier.info;

import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public interface SpecialEffectSettings {
    
    int getFogColor();
    
    int getWaterColor();
    
    int getWaterFogColor();
    
    int getSkyColor();
    
    OptionalInt getFoliageColorOverride();
    
    OptionalInt getGrassColorOverride();
    
    GrassColorModifier getGrassColorModifier();
    
    Optional<AmbientParticleSettings> getAmbientParticle();
    
    Optional<Holder<SoundEvent>> getAmbientLoopSound();
    
    Optional<AmbientMoodSettings> getAmbientMoodSound();
    
    Optional<AmbientAdditionsSettings> getAmbientAdditionsSound();
    
    Optional<Music> getBackgroundMusic();
    
    interface Mutable extends SpecialEffectSettings {

        SpecialEffectSettings.Mutable setFogColor(int color);
        
        SpecialEffectSettings.Mutable setWaterColor(int color);
        
        SpecialEffectSettings.Mutable setWaterFogColor(int color);
        
        SpecialEffectSettings.Mutable setSkyColor(int color);
        
        SpecialEffectSettings.Mutable setFoliageColorOverride(@Nullable Integer colorOverride);
        
        SpecialEffectSettings.Mutable setGrassColorOverride(@Nullable Integer colorOverride);
        
        SpecialEffectSettings.Mutable setGrassColorModifier(GrassColorModifier modifier);
        
        SpecialEffectSettings.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings);
        
        SpecialEffectSettings.Mutable setAmbientLoopSound(@Nullable Holder<SoundEvent> sound);
        
        SpecialEffectSettings.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings);
        
        SpecialEffectSettings.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings);
        
        SpecialEffectSettings.Mutable setBackgroundMusic(@Nullable Music music);
    }
}
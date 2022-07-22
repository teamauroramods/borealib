package dev.tesseract.biomes.fabric;

import dev.tesseract.biomes.SpecialEffectSettings;
import dev.tesseract.biomes.impl.ImmutableSpecialEffectSettings;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MutableSpecialEffectSettingsImpl extends ImmutableSpecialEffectSettings implements SpecialEffectSettings.Mutable {

    private final BiomeModificationContext.EffectsContext parent;

    public MutableSpecialEffectSettingsImpl(Biome biome, BiomeModificationContext.EffectsContext parent) {
        super(biome);
        this.parent = parent;
    }

    @Override
    public SpecialEffectSettings.Mutable setFogColor(int color) {
        this.parent.setFogColor(color);
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setWaterColor(int color) {
        this.parent.setWaterColor(color);
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setWaterFogColor(int color) {
        this.parent.setWaterFogColor(color);
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setSkyColor(int color) {
        this.parent.setSkyColor(color);
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setFoliageColorOverride(@Nullable Integer colorOverride) {
        this.parent.setFoliageColor(Optional.ofNullable(colorOverride));
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setGrassColorOverride(@Nullable Integer colorOverride) {
        this.parent.setGrassColor(Optional.ofNullable(colorOverride));
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier modifier) {
        this.parent.setGrassColorModifier(modifier);
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings) {
        this.parent.setParticleConfig(Optional.ofNullable(settings));
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setAmbientLoopSound(@Nullable SoundEvent sound) {
        this.parent.setAmbientSound(Optional.ofNullable(sound));
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings) {
        this.parent.setMoodSound(Optional.ofNullable(settings));
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings) {
        this.parent.setAdditionsSound(Optional.ofNullable(settings));
        return this;
    }

    @Override
    public SpecialEffectSettings.Mutable setBackgroundMusic(@Nullable Music music) {
        this.parent.setMusic(Optional.ofNullable(music));
        return this;
    }
}

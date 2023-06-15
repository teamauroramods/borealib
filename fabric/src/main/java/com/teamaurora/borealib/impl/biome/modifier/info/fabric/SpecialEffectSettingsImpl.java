package com.teamaurora.borealib.impl.biome.modifier.info.fabric;

import com.teamaurora.borealib.api.biome.v1.modifier.info.GenerationSettings;
import com.teamaurora.borealib.api.biome.v1.modifier.info.SpecialEffectSettings;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

@ApiStatus.Internal
public class SpecialEffectSettingsImpl implements SpecialEffectSettings {

    private final BiomeSpecialEffects specialEffects;

    SpecialEffectSettingsImpl(Biome biome) {
        this.specialEffects = biome.getSpecialEffects();
    }

    @Override
    public int getFogColor() {
        return this.specialEffects.getFogColor();
    }

    @Override
    public int getWaterColor() {
        return this.specialEffects.getWaterColor();
    }

    @Override
    public int getWaterFogColor() {
        return this.specialEffects.getWaterFogColor();
    }

    @Override
    public int getSkyColor() {
        return this.specialEffects.getSkyColor();
    }

    @Override
    public OptionalInt getFoliageColorOverride() {
        return this.specialEffects.getFoliageColorOverride().map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    public OptionalInt getGrassColorOverride() {
        return this.specialEffects.getGrassColorOverride().map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
        return this.specialEffects.getGrassColorModifier();
    }

    @Override
    public Optional<AmbientParticleSettings> getAmbientParticle() {
        return this.specialEffects.getAmbientParticleSettings();
    }

    @Override
    public Optional<Holder<SoundEvent>> getAmbientLoopSound() {
        return this.specialEffects.getAmbientLoopSoundEvent();
    }

    @Override
    public Optional<AmbientMoodSettings> getAmbientMoodSound() {
        return this.specialEffects.getAmbientMoodSettings();
    }

    @Override
    public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
        return this.specialEffects.getAmbientAdditionsSettings();
    }

    @Override
    public Optional<Music> getBackgroundMusic() {
        return this.specialEffects.getBackgroundMusic();
    }

    public static class Mutable extends SpecialEffectSettingsImpl implements SpecialEffectSettings.Mutable {

        private final BiomeModificationContext.EffectsContext context;

        Mutable(Biome biome, BiomeModificationContext.EffectsContext context) {
            super(biome);
            this.context = context;
        }

        @Override
        public SpecialEffectSettings.Mutable setFogColor(int color) {
            this.context.setFogColor(color);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setWaterColor(int color) {
            this.context.setWaterColor(color);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setWaterFogColor(int color) {
            this.context.setWaterFogColor(color);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setSkyColor(int color) {
            this.context.setSkyColor(color);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setFoliageColorOverride(@Nullable Integer colorOverride) {
            this.context.setFoliageColor(Optional.ofNullable(colorOverride));
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setGrassColorOverride(@Nullable Integer colorOverride) {
            this.context.setGrassColor(Optional.ofNullable(colorOverride));
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier modifier) {
            this.context.setGrassColorModifier(modifier);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings) {
            this.context.setParticleConfig(Optional.ofNullable(settings));
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setAmbientLoopSound(@Nullable Holder<SoundEvent> sound) {
            this.context.setAmbientSound(Optional.ofNullable(sound));
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings) {
            this.context.setMoodSound(Optional.ofNullable(settings));
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings) {
            this.context.setAdditionsSound(Optional.ofNullable(settings));
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setBackgroundMusic(@Nullable Music music) {
            this.context.setMusic(Optional.ofNullable(music));
            return this;
        }
    }
}

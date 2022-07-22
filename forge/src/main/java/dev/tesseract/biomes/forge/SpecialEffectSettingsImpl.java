package dev.tesseract.biomes.forge;

import dev.tesseract.biomes.SpecialEffectSettings;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraftforge.common.world.BiomeSpecialEffectsBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public class SpecialEffectSettingsImpl implements SpecialEffectSettings {

    protected final BiomeSpecialEffectsBuilder parent;

    public SpecialEffectSettingsImpl(BiomeSpecialEffectsBuilder parent) {
        this.parent = parent;
    }

    @Override
    public int getFogColor() {
        return this.parent.getFogColor();
    }

    @Override
    public int getWaterColor() {
        return this.parent.waterColor();
    }

    @Override
    public int getWaterFogColor() {
        return this.parent.getWaterFogColor();
    }

    @Override
    public int getSkyColor() {
        return this.parent.getSkyColor();
    }

    @Override
    public OptionalInt getFoliageColorOverride() {
        return this.parent.getFoliageColorOverride().map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    public OptionalInt getGrassColorOverride() {
        return this.parent.getGrassColorOverride().map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
        return this.parent.getGrassColorModifier();
    }

    @Override
    public Optional<AmbientParticleSettings> getAmbientParticle() {
        return this.parent.getAmbientParticle();
    }

    @Override
    public Optional<SoundEvent> getAmbientLoopSound() {
        return this.parent.getAmbientLoopSound();
    }

    @Override
    public Optional<AmbientMoodSettings> getAmbientMoodSound() {
        return this.parent.getAmbientMoodSound();
    }

    @Override
    public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
        return this.parent.getAmbientAdditionsSound();
    }

    @Override
    public Optional<Music> getBackgroundMusic() {
        return this.parent.getBackgroundMusic();
    }

    public static class Mutable extends SpecialEffectSettingsImpl implements SpecialEffectSettings.Mutable {

        public Mutable(BiomeSpecialEffectsBuilder parent) {
            super(parent);
        }

        @Override
        public SpecialEffectSettings.Mutable setFogColor(int color) {
            this.parent.fogColor(color);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setWaterColor(int color) {
            this.parent.waterColor(color);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setWaterFogColor(int color) {
            this.parent.waterFogColor(color);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setSkyColor(int color) {
            this.parent.skyColor(color);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setFoliageColorOverride(@Nullable Integer colorOverride) {
            this.parent.foliageColorOverride = Optional.ofNullable(colorOverride);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setGrassColorOverride(@Nullable Integer colorOverride) {
            this.parent.grassColorOverride = Optional.ofNullable(colorOverride);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier modifier) {
            this.parent.grassColorModifier(modifier);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings) {
            this.parent.ambientParticle = Optional.ofNullable(settings);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setAmbientLoopSound(@Nullable SoundEvent sound) {
            this.parent.ambientLoopSoundEvent = Optional.ofNullable(sound);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings) {
            this.parent.ambientMoodSettings = Optional.ofNullable(settings);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings) {
            this.parent.ambientAdditionsSettings = Optional.ofNullable(settings);
            return this;
        }

        @Override
        public SpecialEffectSettings.Mutable setBackgroundMusic(@Nullable Music music) {
            this.parent.backgroundMusic = Optional.ofNullable(music);
            return this;
        }
    }
}

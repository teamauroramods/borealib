package dev.tesseract.biomes.impl;

import dev.tesseract.biomes.SpecialEffectSettings;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.*;

import java.util.Optional;
import java.util.OptionalInt;

public class ImmutableSpecialEffectSettings implements SpecialEffectSettings {
    protected final BiomeSpecialEffects effects;

    public ImmutableSpecialEffectSettings(Biome biome) {
        this(biome.getSpecialEffects());
    }

    public ImmutableSpecialEffectSettings(BiomeSpecialEffects effects) {
        this.effects = effects;
    }

    @Override
    public int getFogColor() {
        return this.effects.fogColor;
    }

    @Override
    public int getWaterColor() {
        return this.effects.waterColor;
    }

    @Override
    public int getWaterFogColor() {
        return this.effects.waterFogColor;
    }

    @Override
    public int getSkyColor() {
        return this.effects.skyColor;
    }

    @Override
    public OptionalInt getFoliageColorOverride() {
        return this.effects.foliageColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    public OptionalInt getGrassColorOverride() {
        return this.effects.grassColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
        return this.effects.grassColorModifier;
    }

    @Override
    public Optional<AmbientParticleSettings> getAmbientParticle() {
        return this.effects.ambientParticleSettings;
    }

    @Override
    public Optional<SoundEvent> getAmbientLoopSound() {
        return this.effects.ambientLoopSoundEvent;
    }

    @Override
    public Optional<AmbientMoodSettings> getAmbientMoodSound() {
        return this.effects.ambientMoodSettings;
    }

    @Override
    public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
        return this.effects.ambientAdditionsSettings;
    }

    @Override
    public Optional<Music> getBackgroundMusic() {
        return this.effects.backgroundMusic;
    }
}

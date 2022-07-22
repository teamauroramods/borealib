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
        return effects.fogColor;
    }

    @Override
    public int getWaterColor() {
        return effects.waterColor;
    }

    @Override
    public int getWaterFogColor() {
        return effects.waterFogColor;
    }

    @Override
    public int getSkyColor() {
        return effects.skyColor;
    }

    @Override
    public OptionalInt getFoliageColorOverride() {
        return effects.foliageColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    public OptionalInt getGrassColorOverride() {
        return effects.grassColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
        return effects.grassColorModifier;
    }

    @Override
    public Optional<AmbientParticleSettings> getAmbientParticle() {
        return effects.ambientParticleSettings;
    }

    @Override
    public Optional<SoundEvent> getAmbientLoopSound() {
        return effects.ambientLoopSoundEvent;
    }

    @Override
    public Optional<AmbientMoodSettings> getAmbientMoodSound() {
        return effects.ambientMoodSettings;
    }

    @Override
    public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
        return effects.ambientAdditionsSettings;
    }

    @Override
    public Optional<Music> getBackgroundMusic() {
        return effects.backgroundMusic;
    }
}

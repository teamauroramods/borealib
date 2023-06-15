package com.teamaurora.borealib.impl.biome.modifier.info.forge;

import com.teamaurora.borealib.api.biome.v1.modifier.info.SpecialEffectSettings;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraftforge.common.world.BiomeSpecialEffectsBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

@ApiStatus.Internal
public class SpecialEffectSettingsImpl implements SpecialEffectSettings.Mutable {

    private final BiomeSpecialEffectsBuilder builder;

    SpecialEffectSettingsImpl(BiomeSpecialEffectsBuilder builder) {
        this.builder = builder;
    }

    @Override
    public int getFogColor() {
        return this.builder.getFogColor();
    }

    @Override
    public int getWaterColor() {
        return this.builder.waterColor();
    }

    @Override
    public int getWaterFogColor() {
        return this.builder.getWaterFogColor();
    }

    @Override
    public int getSkyColor() {
        return this.builder.getSkyColor();
    }

    @Override
    public OptionalInt getFoliageColorOverride() {
        return this.builder.getFoliageColorOverride().map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    public OptionalInt getGrassColorOverride() {
        return this.builder.getGrassColorOverride().map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    @Override
    public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
        return this.builder.getGrassColorModifier();
    }

    @Override
    public Optional<AmbientParticleSettings> getAmbientParticle() {
        return this.builder.getAmbientParticle();
    }

    @Override
    public Optional<Holder<SoundEvent>> getAmbientLoopSound() {
        return this.builder.getAmbientLoopSound();
    }

    @Override
    public Optional<AmbientMoodSettings> getAmbientMoodSound() {
        return this.builder.getAmbientMoodSound();
    }

    @Override
    public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
        return this.builder.getAmbientAdditionsSound();
    }

    @Override
    public Optional<Music> getBackgroundMusic() {
        return this.builder.getBackgroundMusic();
    }

    @Override
    public Mutable setFogColor(int color) {
        this.builder.fogColor(color);
        return this;
    }

    @Override
    public Mutable setWaterColor(int color) {
        this.builder.waterColor(color);
        return this;
    }

    @Override
    public Mutable setWaterFogColor(int color) {
        this.builder.waterFogColor(color);
        return this;
    }

    @Override
    public Mutable setSkyColor(int color) {
        this.builder.skyColor(color);
        return this;
    }

    @Override
    public Mutable setFoliageColorOverride(@Nullable Integer colorOverride) {
        this.builder.foliageColorOverride = Optional.ofNullable(colorOverride);
        return this;
    }

    @Override
    public Mutable setGrassColorOverride(@Nullable Integer colorOverride) {
        this.builder.grassColorOverride = Optional.ofNullable(colorOverride);
        return this;
    }

    @Override
    public Mutable setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier modifier) {
        this.builder.grassColorModifier(modifier);
        return this;
    }

    @Override
    public Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings) {
        this.builder.ambientParticle = Optional.ofNullable(settings);
        return this;
    }

    @Override
    public Mutable setAmbientLoopSound(@Nullable Holder<SoundEvent> sound) {
        this.builder.ambientLoopSoundEvent = Optional.ofNullable(sound);
        return this;
    }

    @Override
    public Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings) {
        this.builder.ambientMoodSettings = Optional.ofNullable(settings);
        return this;
    }

    @Override
    public Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings) {
        this.builder.ambientAdditionsSettings = Optional.ofNullable(settings);
        return this;
    }

    @Override
    public Mutable setBackgroundMusic(@Nullable Music music) {
        this.builder.backgroundMusic = Optional.ofNullable(music);
        return this;
    }
}

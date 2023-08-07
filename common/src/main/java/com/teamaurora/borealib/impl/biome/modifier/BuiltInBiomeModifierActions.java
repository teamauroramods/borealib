package com.teamaurora.borealib.impl.biome.modifier;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifierAction;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BuiltInBiomeModifierActions {

    private static final Codec<FogColorModification> FOG_COLOR_MODIFICATION_CODEC = register("set_fog_color", Codec.INT.xmap(FogColorModification::new, FogColorModification::fogColor).fieldOf("fog_color").codec());
    private static final Codec<WaterColorModification> WATER_COLOR_MODIFICATION_CODEC = register("set_water_color", Codec.INT.xmap(WaterColorModification::new, WaterColorModification::waterColor).fieldOf("water_color").codec());
    private static final Codec<WaterFogColorModification> WATER_FOG_COLOR_MODIFICATION_CODEC = register("set_water_fog_color", Codec.INT.xmap(WaterFogColorModification::new, WaterFogColorModification::waterFogColor).fieldOf("water_fog_color").codec());
    private static final Codec<SkyColorModification> SKY_COLOR_MODIFICATION_CODEC = register("set_sky_color", Codec.INT.xmap(SkyColorModification::new, SkyColorModification::skyColor).fieldOf("sky_color").codec());
    private static final Codec<FoliageColorModification> FOLIAGE_COLOR_MODIFICATION_CODEC = register("set_foliage_color", Codec.INT.xmap(FoliageColorModification::new, FoliageColorModification::foliageColor).fieldOf("foliage_color").codec());
    private static final Codec<GrassColorModification> GRASS_COLOR_MODIFICATION_CODEC = register("set_grass_color", Codec.INT.xmap(GrassColorModification::new, GrassColorModification::grassColor).fieldOf("grass_color").codec());
    private static final Codec<GrassColorModifierModification> GRASS_COLOR_MODIFIER_MODIFICATION_CODEC = register("set_grass_color_modifier", BiomeSpecialEffects.GrassColorModifier.CODEC.xmap(GrassColorModifierModification::new, GrassColorModifierModification::modifier).fieldOf("modifier").codec());
    private static final Codec<AmbientParticleModification> AMBIENT_PARTICLE_MODIFICATION_CODEC = register("set_ambient_particle", AmbientParticleSettings.CODEC.xmap(AmbientParticleModification::new, AmbientParticleModification::particleSettings).fieldOf("particle").codec());
    private static final Codec<AmbientSoundModification> AMBIENT_SOUND_MODIFICATION_CODEC = register("set_ambient_sound", SoundEvent.CODEC.xmap(AmbientSoundModification::new, AmbientSoundModification::ambientSound).fieldOf("sound").codec());
    private static final Codec<AmbientMoodSoundModification> AMBIENT_MOOD_SOUND_MODIFICATION_CODEC = register("set_mood_sound", AmbientMoodSettings.CODEC.xmap(AmbientMoodSoundModification::new, AmbientMoodSoundModification::moodSettings).fieldOf("settings").codec());
    private static final Codec<AdditionSoundModification> ADDITION_SOUND_MODIFICATION_CODEC = register("set_additions_sound", AmbientAdditionsSettings.CODEC.xmap(AdditionSoundModification::new, AdditionSoundModification::additionsSettings).fieldOf("settings").codec());
    private static final Codec<MusicModification> MUSIC_MODIFICATION_CODEC = register("set_music", Music.CODEC.xmap(MusicModification::new, MusicModification::music).fieldOf("music").codec());

    private static <T extends BiomeModifierAction> Codec<T> register(String path, Codec<T> codec) {
        return BorealibRegistries.BIOME_MODIFIER_ACTION_TYPES.register(Borealib.location(path), codec);
    }

    public static BiomeModifierAction setFogColor(int color) {
        return new FogColorModification(color);
    }

    public static BiomeModifierAction setWaterColor(int color) {
        return new WaterColorModification(color);
    }

    public static BiomeModifierAction setWaterFogColor(int color) {
        return new WaterFogColorModification(color);
    }

    public static BiomeModifierAction setSkyColor(int color) {
        return new SkyColorModification(color);
    }

    public static void init() {}


    private record FogColorModification(int fogColor) implements BiomeModifierAction {

        @Override
        public Codec<FogColorModification> type() {
            return FOG_COLOR_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setFogColor(this.fogColor);
        }
    }

    private record WaterColorModification(int waterColor) implements BiomeModifierAction {

        @Override
        public Codec<WaterColorModification> type() {
            return WATER_COLOR_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setWaterColor(this.waterColor);
        }
    }

    private record WaterFogColorModification(int waterFogColor) implements BiomeModifierAction {

        @Override
        public Codec<WaterFogColorModification> type() {
            return WATER_FOG_COLOR_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setWaterFogColor(this.waterFogColor);
        }
    }

    private record SkyColorModification(int skyColor) implements BiomeModifierAction {

        @Override
        public Codec<SkyColorModification> type() {
            return SKY_COLOR_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setSkyColor(this.skyColor);
        }
    }

    private record FoliageColorModification(int foliageColor) implements BiomeModifierAction {

        @Override
        public Codec<FoliageColorModification> type() {
            return FOLIAGE_COLOR_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setFoliageColorOverride(this.foliageColor);
        }
    }

    private record GrassColorModification(int grassColor) implements BiomeModifierAction {

        @Override
        public Codec<GrassColorModification> type() {
            return GRASS_COLOR_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setGrassColorOverride(this.grassColor);
        }
    }

    private record GrassColorModifierModification(BiomeSpecialEffects.GrassColorModifier modifier) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setGrassColorModifier(this.modifier);
        }

        @Override
        public Codec<GrassColorModifierModification> type() {
            return GRASS_COLOR_MODIFIER_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }
    }

    private record AmbientParticleModification(AmbientParticleSettings particleSettings) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setAmbientParticle(this.particleSettings);
        }

        @Override
        public Codec<AmbientParticleModification> type() {
            return AMBIENT_PARTICLE_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }
    }

    private record AmbientSoundModification(Holder<SoundEvent> ambientSound) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setAmbientLoopSound(this.ambientSound);
        }

        @Override
        public Codec<AmbientSoundModification> type() {
            return AMBIENT_SOUND_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }
    }

    private record AmbientMoodSoundModification(AmbientMoodSettings moodSettings) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setAmbientMoodSound(this.moodSettings);
        }

        @Override
        public Codec<AmbientMoodSoundModification> type() {
            return AMBIENT_MOOD_SOUND_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }
    }

    private record AdditionSoundModification(AmbientAdditionsSettings additionsSettings) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setAmbientAdditionsSound(this.additionsSettings);
        }

        @Override
        public Codec<AdditionSoundModification> type() {
            return ADDITION_SOUND_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }
    }

    private record MusicModification(Music music) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setBackgroundMusic(this.music);
        }

        @Override
        public Codec<MusicModification> type() {
            return MUSIC_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }
    }
}

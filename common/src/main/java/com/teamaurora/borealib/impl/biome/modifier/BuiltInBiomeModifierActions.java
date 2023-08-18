package com.teamaurora.borealib.impl.biome.modifier;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamaurora.borealib.api.base.v1.util.CodecHelper;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifierAction;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.api.biome.v1.modifier.info.GenerationSettings;
import com.teamaurora.borealib.api.biome.v1.modifier.info.SpawnSettings;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.ApiStatus;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@ApiStatus.Internal
public class BuiltInBiomeModifierActions {

    private static final Codec<AddFeaturesModification> ADD_FEATURES = register("add_features", RecordCodecBuilder.create(instance -> instance.group(
            GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AddFeaturesModification::decoration),
            PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddFeaturesModification::features)
    ).apply(instance, AddFeaturesModification::new)));
    private static final Codec<RemoveFeaturesModification> REMOVE_FEATURES = register("remove_features", RecordCodecBuilder.create(instance -> instance.group(
            CodecHelper.<List<GenerationStep.Decoration>, GenerationStep.Decoration>either(GenerationStep.Decoration.CODEC.listOf(), GenerationStep.Decoration.CODEC).<Set<GenerationStep.Decoration>>xmap(
                    either -> either.map(Set::copyOf, Set::of), // convert list/singleton to set when decoding
                    set -> set.size() == 1 ? Either.right(set.toArray(GenerationStep.Decoration[]::new)[0]) : Either.left(List.copyOf(set))
            ).optionalFieldOf("steps", EnumSet.allOf(GenerationStep.Decoration.class)).forGetter(RemoveFeaturesModification::steps),
            PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(RemoveFeaturesModification::features)
    ).apply(instance, RemoveFeaturesModification::new)));
    private static final Codec<LinearFeatureReplacement> REPLACE_FEATURES_LINEAR = register("replace_feature_linear", RecordCodecBuilder.create(instance -> instance.group(
            GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(LinearFeatureReplacement::decoration),
            PlacedFeature.CODEC.fieldOf("original").forGetter(LinearFeatureReplacement::original),
            PlacedFeature.CODEC.fieldOf("replacement").forGetter(LinearFeatureReplacement::replacement)
    ).apply(instance, LinearFeatureReplacement::new)));
    private static final Codec<NonlinearFeatureReplacement> REPLACE_FEATURES_NONLINEAR = register("replace_features_nonlinear", RecordCodecBuilder.create(instance -> instance.group(
            GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(NonlinearFeatureReplacement::decoration),
            PlacedFeature.LIST_CODEC.fieldOf("originals").forGetter(NonlinearFeatureReplacement::originals),
            PlacedFeature.LIST_CODEC.fieldOf("replacements").forGetter(NonlinearFeatureReplacement::replacements)
    ).apply(instance, NonlinearFeatureReplacement::new)));
    private static final Codec<ReplaceEntitySpawnModification> REPLACE_SPAWN = register("replace_spawn", RecordCodecBuilder.create(instance -> instance.group(
            RegistryWrapper.ENTITY_TYPES.byNameCodec().fieldOf("original").forGetter(ReplaceEntitySpawnModification::original),
            RegistryWrapper.ENTITY_TYPES.byNameCodec().fieldOf("replacement").forGetter(ReplaceEntitySpawnModification::replacement)
    ).apply(instance, ReplaceEntitySpawnModification::new)));
    private static final Codec<RemoveSpawnsModification> REMOVE_SPAWNS = register("remove_spawns", RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).xmap(RemoveSpawnsModification::new, RemoveSpawnsModification::entityTypes).fieldOf("entity_types").codec());
    private static final Codec<AddSpawnsModification> ADD_SPAWNS = register("add_spawns", CodecHelper.nonEmptyList(MobSpawnSettings.SpawnerData.CODEC).xmap(AddSpawnsModification::new, AddSpawnsModification::spawners).fieldOf("spawners").codec());
    private static final Codec<FogColorModification> SET_FOG_COLOR = register("set_fog_color", Codec.INT.xmap(FogColorModification::new, FogColorModification::fogColor).fieldOf("fog_color").codec());
    private static final Codec<WaterColorModification> SET_WATER_COLOR = register("set_water_color", Codec.INT.xmap(WaterColorModification::new, WaterColorModification::waterColor).fieldOf("water_color").codec());
    private static final Codec<WaterFogColorModification> SET_WATER_FOG_COLOR = register("set_water_fog_color", Codec.INT.xmap(WaterFogColorModification::new, WaterFogColorModification::waterFogColor).fieldOf("water_fog_color").codec());
    private static final Codec<SkyColorModification> SET_SKY_COLOR = register("set_sky_color", Codec.INT.xmap(SkyColorModification::new, SkyColorModification::skyColor).fieldOf("sky_color").codec());
    private static final Codec<FoliageColorModification> SET_FOLIAGE_COLOR = register("set_foliage_color", Codec.INT.xmap(FoliageColorModification::new, FoliageColorModification::foliageColor).fieldOf("foliage_color").codec());
    private static final Codec<GrassColorModification> SET_GRASS_COLOR = register("set_grass_color", Codec.INT.xmap(GrassColorModification::new, GrassColorModification::grassColor).fieldOf("grass_color").codec());
    private static final Codec<GrassColorModifierModification> SET_GRASS_COLOR_MODIFIER = register("set_grass_color_modifier", BiomeSpecialEffects.GrassColorModifier.CODEC.xmap(GrassColorModifierModification::new, GrassColorModifierModification::modifier).fieldOf("modifier").codec());
    private static final Codec<AmbientParticleModification> SET_AMBIENT_PARTICLE = register("set_ambient_particle", AmbientParticleSettings.CODEC.xmap(AmbientParticleModification::new, AmbientParticleModification::particleSettings).fieldOf("particle").codec());
    private static final Codec<AmbientSoundModification> SET_AMBIENT_SOUND = register("set_ambient_sound", SoundEvent.CODEC.xmap(AmbientSoundModification::new, AmbientSoundModification::ambientSound).fieldOf("sound").codec());
    private static final Codec<AmbientMoodSoundModification> SET_AMBIENT_MOOD_SOUND = register("set_mood_sound", AmbientMoodSettings.CODEC.xmap(AmbientMoodSoundModification::new, AmbientMoodSoundModification::moodSettings).fieldOf("settings").codec());
    private static final Codec<AdditionSoundModification> SET_ADDITIONS_SOUND = register("set_additions_sound", AmbientAdditionsSettings.CODEC.xmap(AdditionSoundModification::new, AdditionSoundModification::additionsSettings).fieldOf("settings").codec());
    private static final Codec<MusicModification> SET_MUSIC = register("set_music", Music.CODEC.xmap(MusicModification::new, MusicModification::music).fieldOf("music").codec());

    private static <T extends BiomeModifierAction> Codec<T> register(String path, Codec<T> codec) {
        return BorealibRegistries.BIOME_MODIFIER_ACTION_TYPES.register(Borealib.location(path), codec);
    }

    public static BiomeModifierAction addFeatures(GenerationStep.Decoration decoration, HolderSet<PlacedFeature> features) {
        return new AddFeaturesModification(decoration, features);
    }

    public static BiomeModifierAction removeFeatures(Set<GenerationStep.Decoration> decorations, HolderSet<PlacedFeature> features) {
        return new RemoveFeaturesModification(decorations, features);
    }

    public static BiomeModifierAction replaceFeaturesLinear(GenerationStep.Decoration decoration, Holder<PlacedFeature> original, Holder<PlacedFeature> replacement) {
        return new LinearFeatureReplacement(decoration, original, replacement);
    }

    public static BiomeModifierAction replaceFeaturesNonlinear(GenerationStep.Decoration decoration, HolderSet<PlacedFeature> originals, HolderSet<PlacedFeature> replacements) {
        return new NonlinearFeatureReplacement(decoration, originals, replacements);
    }

    public static BiomeModifierAction addSpawns(List<MobSpawnSettings.SpawnerData> data) {
        return new AddSpawnsModification(data);
    }

    public static BiomeModifierAction removeSpawns(HolderSet<EntityType<?>> entityTypes) {
        return new RemoveSpawnsModification(entityTypes);
    }

    public static BiomeModifierAction replaceSpawn(EntityType<?> original, EntityType<?> replacement) {
        return new ReplaceEntitySpawnModification(original, replacement);
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

    public static BiomeModifierAction setFoliageColor(int color) {
        return new FogColorModification(color);
    }

    public static BiomeModifierAction setGrassColor(int color) {
        return new GrassColorModification(color);
    }

    public static BiomeModifierAction setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier modifier) {
        return new GrassColorModifierModification(modifier);
    }

    public static BiomeModifierAction setAmbientParticle(AmbientParticleSettings settings) {
        return new AmbientParticleModification(settings);
    }

    public static BiomeModifierAction setAmbientSound(Holder<SoundEvent> sound) {
        return new AmbientSoundModification(sound);
    }

    public static BiomeModifierAction setMoodSound(AmbientMoodSettings settings) {
        return new AmbientMoodSoundModification(settings);
    }

    public static BiomeModifierAction setAdditionsSound(AmbientAdditionsSettings settings) {
        return new AdditionSoundModification(settings);
    }

    public static BiomeModifierAction setMusic(Music music) {
        return new MusicModification(music);
    }

    public static void init() {}

    private record FogColorModification(int fogColor) implements BiomeModifierAction {

        @Override
        public Codec<FogColorModification> type() {
            return SET_FOG_COLOR;
        }

        @Override
        public Stage stage() {
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
            return SET_WATER_COLOR;
        }

        @Override
        public Stage stage() {
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
            return SET_WATER_FOG_COLOR;
        }

        @Override
        public Stage stage() {
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
            return SET_SKY_COLOR;
        }

        @Override
        public Stage stage() {
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
            return SET_FOLIAGE_COLOR;
        }

        @Override
        public Stage stage() {
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
            return SET_GRASS_COLOR;
        }

        @Override
        public Stage stage() {
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
            return SET_GRASS_COLOR_MODIFIER;
        }

        @Override
        public Stage stage() {
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
            return SET_AMBIENT_PARTICLE;
        }

        @Override
        public Stage stage() {
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
            return SET_AMBIENT_SOUND;
        }

        @Override
        public Stage stage() {
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
            return SET_AMBIENT_MOOD_SOUND;
        }

        @Override
        public Stage stage() {
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
            return SET_ADDITIONS_SOUND;
        }

        @Override
        public Stage stage() {
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
            return SET_MUSIC;
        }

        @Override
        public Stage stage() {
            return Stage.POST_PROCESSING;
        }
    }

    private record AddFeaturesModification(GenerationStep.Decoration decoration, HolderSet<PlacedFeature> features) implements BiomeModifierAction{

        @Override
        public void accept(BiomeInfo.Mutable info) {
            GenerationSettings.Mutable settings = info.getGenerationSettings();
            this.features.forEach(feature -> settings.addFeature(this.decoration, feature));
        }

        @Override
        public Codec<AddFeaturesModification> type() {
            return ADD_FEATURES;
        }

        @Override
        public Stage stage() {
            return Stage.ADDITIONS;
        }
    }

    private record RemoveFeaturesModification(Set<GenerationStep.Decoration> steps, HolderSet<PlacedFeature> features) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            GenerationSettings.Mutable settings = info.getGenerationSettings();
            this.features.forEach(feature -> this.steps.forEach(step -> settings.removeFeature(step, feature)));
        }

        @Override
        public Codec<RemoveFeaturesModification> type() {
            return REMOVE_FEATURES;
        }

        @Override
        public Stage stage() {
            return Stage.REMOVALS;
        }
    }

    private record LinearFeatureReplacement(GenerationStep.Decoration decoration, Holder<PlacedFeature> original, Holder<PlacedFeature> replacement) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            GenerationSettings.Mutable settings = info.getGenerationSettings();
            settings.removeFeature(this.decoration, this.original);
            settings.addFeature(this.decoration, this.replacement);
        }

        @Override
        public Codec<LinearFeatureReplacement> type() {
            return REPLACE_FEATURES_LINEAR;
        }

        @Override
        public Stage stage() {
            return Stage.REPLACEMENTS;
        }
    }

    private record NonlinearFeatureReplacement(GenerationStep.Decoration decoration, HolderSet<PlacedFeature> originals, HolderSet<PlacedFeature> replacements) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            GenerationSettings.Mutable settings = info.getGenerationSettings();
            this.originals.forEach(feature -> settings.removeFeature(this.decoration, feature));
            this.replacements.forEach(feature -> settings.addFeature(this.decoration, feature));
        }

        @Override
        public Codec<NonlinearFeatureReplacement> type() {
            return REPLACE_FEATURES_NONLINEAR;
        }

        // Stage is calculated each time the modifier is run b/c tags can change
        @Override
        public Stage stage() {
            return this.originals.size() == this.replacements.size() ? Stage.REPLACEMENTS
                    : this.replacements.size() > this.originals.size() ? Stage.ADDITIONS : Stage.REMOVALS;
        }
    }

    private record AddSpawnsModification(List<MobSpawnSettings.SpawnerData> spawners) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            this.spawners.forEach(data -> info.getSpawnSettings().addSpawn(data.type.getCategory(), data));
        }

        @Override
        public Codec<AddSpawnsModification> type() {
            return ADD_SPAWNS;
        }

        @Override
        public Stage stage() {
            return Stage.ADDITIONS;
        }
    }

    private record RemoveSpawnsModification(HolderSet<EntityType<?>> entityTypes) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getSpawnSettings().removeSpawns(((category, spawnerData) -> this.entityTypes.contains(RegistryWrapper.ENTITY_TYPES.getHolder(spawnerData.type).get())));
        }

        @Override
        public Codec<RemoveSpawnsModification> type() {
            return REMOVE_SPAWNS;
        }

        @Override
        public Stage stage() {
            return Stage.REMOVALS;
        }
    }

    private record ReplaceEntitySpawnModification(EntityType<?> original, EntityType<?> replacement) implements BiomeModifierAction {

        @Override
        public void accept(BiomeInfo.Mutable info) {
            SpawnSettings.Mutable settings = info.getSpawnSettings();
            // Costs
            MobSpawnSettings.MobSpawnCost cost = settings.getMobSpawnCosts().get(this.original);
            if (cost != null) {
                MobSpawnSettings.MobSpawnCost newCost = new MobSpawnSettings.MobSpawnCost(cost.energyBudget(), cost.charge());
                settings.clearSpawnCost(this.original);
                settings.setSpawnCost(this.replacement, newCost);
            }
            List<MobSpawnSettings.SpawnerData> originals = settings.getSpawners().get(this.original.getCategory()).stream().filter(data -> data.type == this.original).toList();
            settings.removeSpawns((cat, data) -> originals.contains(data));
            originals.forEach(oldData -> {
                MobSpawnSettings.SpawnerData newData = new MobSpawnSettings.SpawnerData(this.replacement, oldData.getWeight(), oldData.minCount, oldData.maxCount);
                settings.addSpawn(this.replacement.getCategory(), newData);
            });
        }

        @Override
        public Codec<ReplaceEntitySpawnModification> type() {
            return REPLACE_SPAWN;
        }

        @Override
        public Stage stage() {
            return Stage.REPLACEMENTS;
        }
    }
}

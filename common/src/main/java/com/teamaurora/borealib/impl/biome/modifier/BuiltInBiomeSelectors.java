package com.teamaurora.borealib.impl.biome.modifier;

import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamaurora.borealib.api.base.v1.util.CodecHelper;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeSelector;
import com.teamaurora.borealib.api.biome.v1.modifier.info.GenerationSettings;
import com.teamaurora.borealib.api.config.v1.ConfigRegistry;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class BuiltInBiomeSelectors {

    private static final Codec<OrSelector> OR = register("or", CodecHelper.nonEmptyList(BiomeSelector.CODEC).xmap(OrSelector::new, OrSelector::parents).fieldOf("values").codec());
    private static final Codec<AndSelector> AND = register("and", CodecHelper.nonEmptyList(BiomeSelector.CODEC).xmap(AndSelector::new, AndSelector::parents).fieldOf("values").codec());
    private static final Codec<BiomeCheck> IS_BIOME = register("is_biome", Biome.LIST_CODEC.xmap(BiomeCheck::new, BiomeCheck::biomes).fieldOf("biomes").codec());
    private static final Codec<DimensionCheck> IN_DIMENSION = register("generates_in", ResourceKey.codec(Registries.LEVEL_STEM).xmap(DimensionCheck::new, DimensionCheck::dimension).fieldOf("dimension").codec());
    private static final Codec<StructureCheck> HAS_STRUCTURE = register("has_structure", ResourceKey.codec(Registries.STRUCTURE).xmap(StructureCheck::new, StructureCheck::structure).fieldOf("structure").codec());
    private static final Codec<AllSelector> ALL = register("all", Codec.unit(AllSelector.INSTANCE));
    private static final Codec<ConfigToggle> CONFIG = register("config_toggle", RecordCodecBuilder.create(instance -> instance.group(
            ModConfig.CODEC.fieldOf("config").forGetter(ConfigToggle::config),
            Codec.STRING.fieldOf("key").forGetter(ConfigToggle::key),
            Codec.BOOL.fieldOf("expected_value").forGetter(ConfigToggle::expectedValue)
    ).apply(instance, ConfigToggle::new)));
    private static final Codec<TestsEnabledSelector> TESTS_ENABLED = register("tests_enabled", Codec.unit(TestsEnabledSelector.INSTANCE));
    private static final Codec<ExistingFeatureSelector> EXISTING_FEATURES = register("has_existing_features", RecordCodecBuilder.create(instance -> instance.group(
            GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(ExistingFeatureSelector::decoration),
            PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(ExistingFeatureSelector::features)
    ).apply(instance, ExistingFeatureSelector::new)));
    private static final Codec<NotSelector> NOT = register("not", BiomeSelector.CODEC.xmap(NotSelector::new, NotSelector::selector).fieldOf("value").codec());
    private static final Codec<ExistingSpawnSelector> EXISTING_SPAWN = register("has_existing_spawn", RecordCodecBuilder.create(instance -> instance.group(
            MobCategory.CODEC.fieldOf("category").forGetter(ExistingSpawnSelector::category),
            RegistryWrapper.ENTITY_TYPES.byNameCodec().fieldOf("entity").forGetter(ExistingSpawnSelector::entityType)
    ).apply(instance, ExistingSpawnSelector::new)));

    private static <T extends BiomeSelector> Codec<T> register(String path, Codec<T> codec) {
        return BorealibRegistries.BIOME_SELECTOR_TYPES.register(Borealib.location(path), codec);
    }

    public static BiomeSelector all() {
        return AllSelector.INSTANCE;
    }

    public static BiomeSelector testsEnabled() {
        return TestsEnabledSelector.INSTANCE;
    }

    public static BiomeSelector or(List<BiomeSelector> parents) {
        return new OrSelector(parents);
    }

    public static BiomeSelector and(List<BiomeSelector> parents) {
        return new AndSelector(parents);
    }

    public static BiomeSelector not(BiomeSelector value) {
        return new NotSelector(value);
    }

    public static BiomeSelector biomeCheck(HolderSet<Biome> biomes) {
        return new BiomeCheck(biomes);
    }

    public static BiomeSelector dimensionCheck(ResourceKey<LevelStem> dimension) {
        return new DimensionCheck(dimension);
    }

    public static BiomeSelector structureCheck(ResourceKey<Structure> structure) {
        return new StructureCheck(structure);
    }

    public static BiomeSelector configToggle(ModConfig config, String key, boolean value) {
        return new ConfigToggle(config, key, value);
    }

    public static BiomeSelector hasExistingFeatures(GenerationStep.Decoration decoration, HolderSet<PlacedFeature> features) {
        return new ExistingFeatureSelector(decoration, features);
    }

    public static BiomeSelector hasExistingSpawn(MobCategory category, EntityType<?> entityType) {
        return new ExistingSpawnSelector(category, entityType);
    }

    public static void init() {}

    private record AllSelector() implements BiomeSelector {

        private static final AllSelector INSTANCE = new AllSelector();

        @Override
        public boolean test(Context context) {
            return true;
        }

        @Override
        public Codec<AllSelector> type() {
            return ALL;
        }
    }

    private record OrSelector(List<BiomeSelector> parents) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            for (BiomeSelector selector : this.parents)
                if (selector.test(context)) return true;
            return false;
        }

        @Override
        public Codec<OrSelector> type() {
            return OR;
        }
    }

    private record AndSelector(List<BiomeSelector> parents) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            for (BiomeSelector selector : this.parents)
                if (!selector.test(context)) return false;
            return true;
        }

        @Override
        public Codec<AndSelector> type() {
            return AND;
        }
    }

    private record BiomeCheck(HolderSet<Biome> biomes) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            return this.biomes.contains(context.getBiome());
        }

        @Override
        public Codec<BiomeCheck> type() {
            return IS_BIOME;
        }
    }

    private record DimensionCheck(ResourceKey<LevelStem> dimension) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            return context.generatesIn(this.dimension);
        }

        @Override
        public Codec<DimensionCheck> type() {
            return IN_DIMENSION;
        }
    }

    private record StructureCheck(ResourceKey<Structure> structure) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            return context.hasStructure(this.structure);
        }

        @Override
        public Codec<StructureCheck> type() {
            return HAS_STRUCTURE;
        }
    }

    private record ConfigToggle(ModConfig config, String key, boolean expectedValue) implements BiomeSelector{

        @Override
        public boolean test(Context context) {
            Object entry = this.config.getConfigData().get(this.key);
            if (entry == null) throw new JsonSyntaxException("Config value " + this.key + " doesn't exist");
            if (entry instanceof Boolean bool)
                return bool == this.expectedValue;
            Borealib.LOGGER.warn("Config value " + this.key + " exists but is not boolean; skipping");
            return false;
        }

        @Override
        public Codec<ConfigToggle> type() {
            return CONFIG;
        }
    }

    private record TestsEnabledSelector() implements BiomeSelector {

        private static final TestsEnabledSelector INSTANCE = new TestsEnabledSelector();

        @Override
        public boolean test(Context context) {
            return Borealib.TESTS_ENABLED;
        }

        @Override
        public Codec<TestsEnabledSelector> type() {
            return TESTS_ENABLED;
        }
    }

    private record NotSelector(BiomeSelector selector) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            return !this.selector.test(context);
        }

        @Override
        public Codec<NotSelector> type() {
            return NOT;
        }
    }

    private record ExistingFeatureSelector(GenerationStep.Decoration decoration, HolderSet<PlacedFeature> features) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            GenerationSettings settings = context.getExistingInfo().getGenerationSettings();
            for (Holder<PlacedFeature> feature : this.features) {
                if (settings.hasFeature(this.decoration, feature)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Codec<? extends BiomeSelector> type() {
            return EXISTING_FEATURES;
        }
    }

    private record ExistingSpawnSelector(MobCategory category, EntityType<?> entityType) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            return context.getExistingInfo().getSpawnSettings().getSpawners().get(this.category).stream().anyMatch(data -> data.type == entityType);
        }

        @Override
        public Codec<? extends BiomeSelector> type() {
            return EXISTING_SPAWN;
        }
    }
}

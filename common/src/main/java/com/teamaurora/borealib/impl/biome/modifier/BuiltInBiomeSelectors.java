package com.teamaurora.borealib.impl.biome.modifier;

import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamaurora.borealib.api.base.v1.util.CodecHelper;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeSelector;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class BuiltInBiomeSelectors {

    public static final RegistryWrapper.Provider<Codec<? extends BiomeSelector>> PROVIDER = BorealibRegistries.BIOME_SELECTOR_TYPES.getDefaultProvider();
    private static final RegistryReference<Codec<OrSelector>> OR_CODEC = PROVIDER.register("or", () -> CodecHelper.nonEmptyList(BiomeSelector.CODEC).xmap(OrSelector::new, OrSelector::parents).fieldOf("values").codec());
    private static final RegistryReference<Codec<AndSelector>> AND_CODEC = PROVIDER.register("and", () -> CodecHelper.nonEmptyList(BiomeSelector.CODEC).xmap(AndSelector::new, AndSelector::parents).fieldOf("values").codec());
    private static final RegistryReference<Codec<BiomeCheck>> BIOME_CHECK_CODEC = PROVIDER.register("is_biome", () -> Biome.LIST_CODEC.xmap(BiomeCheck::new, BiomeCheck::biomes).fieldOf("biomes").codec());
    private static final RegistryReference<Codec<DimensionCheck>> DIMENSION_CHECK_CODEC = PROVIDER.register("generates_in", () -> ResourceKey.codec(Registries.LEVEL_STEM).xmap(DimensionCheck::new, DimensionCheck::dimension).fieldOf("dimension").codec());
    private static final RegistryReference<Codec<StructureCheck>> STRUCTURE_CHECK_CODEC = PROVIDER.register("has_structure", () -> ResourceKey.codec(Registries.STRUCTURE).xmap(StructureCheck::new, StructureCheck::structure).fieldOf("structure").codec());
    private static final RegistryReference<Codec<AllSelector>> ALL_CODEC = PROVIDER.register("all", () -> Codec.unit(AllSelector.INSTANCE));
    private static final RegistryReference<Codec<ConfigToggle>> CONFIG_TOGGLE_CODEC = PROVIDER.register("config_toggle", () -> RecordCodecBuilder.create(instance -> instance.group(
            ModConfig.CODEC.fieldOf("config").forGetter(ConfigToggle::config),
            Codec.STRING.fieldOf("key").forGetter(ConfigToggle::key),
            Codec.BOOL.fieldOf("expected_value").forGetter(ConfigToggle::expectedValue)
    ).apply(instance, ConfigToggle::new)));
    private static final RegistryReference<Codec<TestsEnabledSelector>> TESTS_ENABLED_CODEC = PROVIDER.register("tests_enabled", () -> Codec.unit(TestsEnabledSelector.INSTANCE));
    private static final RegistryReference<Codec<NotSelector>> NOT_CODEC = PROVIDER.register("not", () -> BiomeSelector.CODEC.xmap(NotSelector::new, NotSelector::selector).fieldOf("value").codec());

    public static BiomeSelector or(List<BiomeSelector> parents) {
        return new OrSelector(parents);
    }

    public static BiomeSelector and(List<BiomeSelector> parents) {
        return new AndSelector(parents);
    }

    public static BiomeSelector biomeCheck(HolderSet<Biome> biomes) {
        return new BiomeCheck(biomes);
    }

    public static BiomeSelector dimensionCheck(ResourceKey<LevelStem> dimension) {
        return new DimensionCheck(dimension);
    }

    private record AllSelector() implements BiomeSelector {

        private static final AllSelector INSTANCE = new AllSelector();

        @Override
        public boolean test(Context context) {
            return true;
        }

        @Override
        public Codec<AllSelector> type() {
            return ALL_CODEC.get();
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
            return OR_CODEC.get();
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
            return AND_CODEC.get();
        }
    }

    private record BiomeCheck(HolderSet<Biome> biomes) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            return this.biomes.contains(context.getBiome());
        }

        @Override
        public Codec<BiomeCheck> type() {
            return BIOME_CHECK_CODEC.get();
        }
    }

    private record DimensionCheck(ResourceKey<LevelStem> dimension) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            return context.generatesIn(this.dimension);
        }

        @Override
        public Codec<DimensionCheck> type() {
            return DIMENSION_CHECK_CODEC.get();
        }
    }

    private record StructureCheck(ResourceKey<Structure> structure) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            return context.hasStructure(this.structure);
        }

        @Override
        public Codec<StructureCheck> type() {
            return STRUCTURE_CHECK_CODEC.get();
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
            return CONFIG_TOGGLE_CODEC.get();
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
            return TESTS_ENABLED_CODEC.get();
        }
    }

    private record NotSelector(BiomeSelector selector) implements BiomeSelector {

        @Override
        public boolean test(Context context) {
            return !this.selector.test(context);
        }

        @Override
        public Codec<NotSelector> type() {
            return NOT_CODEC.get();
        }
    }
}

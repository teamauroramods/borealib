package com.teamaurora.borealib.api.registry.v1;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.teamaurora.borealib.impl.registry.RegistryViewImpl;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * Provides a read-only view of platform specific registries.
 *
 * @param <T> The top level of the registry type
 */
public interface RegistryView<T> extends Keyable, IdMap<T> {

    RegistryView<GameEvent> GAME_EVENT = of(Registries.GAME_EVENT);
    RegistryView<SoundEvent> SOUND_EVENT = of(Registries.SOUND_EVENT);
    RegistryView<Fluid> FLUID = of(Registries.FLUID);
    RegistryView<MobEffect> MOB_EFFECT = of(Registries.MOB_EFFECT);
    RegistryView<Block> BLOCK = of(Registries.BLOCK);
    RegistryView<Enchantment> ENCHANTMENT = of(Registries.ENCHANTMENT);
    RegistryView<EntityType<?>> ENTITY_TYPE = of(Registries.ENTITY_TYPE);
    RegistryView<Item> ITEM = of(Registries.ITEM);
    RegistryView<Potion> POTION = of(Registries.POTION);
    RegistryView<ParticleType<?>> PARTICLE_TYPE = of(Registries.PARTICLE_TYPE);
    RegistryView<BlockEntityType<?>> BLOCK_ENTITY_TYPE = of(Registries.BLOCK_ENTITY_TYPE);
    RegistryView<PaintingVariant> PAINTING_VARIANT = of(Registries.PAINTING_VARIANT);
    RegistryView<ResourceLocation> CUSTOM_STAT = of(Registries.CUSTOM_STAT);
    RegistryView<ChunkStatus> CHUNK_STATUS = of(Registries.CHUNK_STATUS);
    RegistryView<RuleTestType<?>> RULE_TEST = of(Registries.RULE_TEST);
    RegistryView<PosRuleTestType<?>> POS_RULE_TEST = of(Registries.POS_RULE_TEST);
    RegistryView<MenuType<?>> MENU = of(Registries.MENU);
    RegistryView<RecipeType<?>> RECIPE_TYPE = of(Registries.RECIPE_TYPE);
    RegistryView<RecipeSerializer<?>> RECIPE_SERIALIZER = of(Registries.RECIPE_SERIALIZER);
    RegistryView<Attribute> ATTRIBUTE = of(Registries.ATTRIBUTE);
    RegistryView<PositionSourceType<?>> POSITION_SOURCE_TYPE = of(Registries.POSITION_SOURCE_TYPE);
    RegistryView<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPE = of(Registries.COMMAND_ARGUMENT_TYPE);
    RegistryView<StatType<?>> STAT_TYPE = of(Registries.STAT_TYPE);
    RegistryView<VillagerType> VILLAGER_TYPE = of(Registries.VILLAGER_TYPE);
    RegistryView<VillagerProfession> VILLAGER_PROFESSION = of(Registries.VILLAGER_PROFESSION);
    RegistryView<PoiType> POINT_OF_INTEREST_TYPE = of(Registries.POINT_OF_INTEREST_TYPE);
    RegistryView<MemoryModuleType<?>> MEMORY_MODULE_TYPE = of(Registries.MEMORY_MODULE_TYPE);
    RegistryView<SensorType<?>> SENSOR_TYPE = of(Registries.SENSOR_TYPE);
    RegistryView<Schedule> SCHEDULE = of(Registries.SCHEDULE);
    RegistryView<Activity> ACTIVITY = of(Registries.ACTIVITY);
    RegistryView<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = of(Registries.LOOT_POOL_ENTRY_TYPE);
    RegistryView<LootItemFunctionType> LOOT_FUNCTION_TYPE = of(Registries.LOOT_FUNCTION_TYPE);
    RegistryView<LootItemConditionType> LOOT_CONDITION_TYPE = of(Registries.LOOT_CONDITION_TYPE);
    RegistryView<LootNumberProviderType> LOOT_NUMBER_PROVIDER_TYPE = of(Registries.LOOT_NUMBER_PROVIDER_TYPE);
    RegistryView<LootNbtProviderType> LOOT_NBT_PROVIDER_TYPE = of(Registries.LOOT_NBT_PROVIDER_TYPE);
    RegistryView<LootScoreProviderType> LOOT_SCORE_PROVIDER_TYPE = of(Registries.LOOT_SCORE_PROVIDER_TYPE);
    RegistryView<FloatProviderType<?>> FLOAT_PROVIDER_TYPE = of(Registries.FLOAT_PROVIDER_TYPE);
    RegistryView<IntProviderType<?>> INT_PROVIDER_TYPE = of(Registries.INT_PROVIDER_TYPE);
    RegistryView<HeightProviderType<?>> HEIGHT_PROVIDER_TYPE = of(Registries.HEIGHT_PROVIDER_TYPE);
    RegistryView<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = of(Registries.BLOCK_PREDICATE_TYPE);
    RegistryView<WorldCarver<?>> CARVER = of(Registries.CARVER);
    RegistryView<Feature<?>> FEATURE = of(Registries.FEATURE);
    RegistryView<StructurePlacementType<?>> STRUCTURE_PLACEMENT = of(Registries.STRUCTURE_PLACEMENT);
    RegistryView<StructurePieceType> STRUCTURE_PIECE = of(Registries.STRUCTURE_PIECE);
    RegistryView<StructureType<?>> STRUCTURE_TYPE = of(Registries.STRUCTURE_TYPE);
    RegistryView<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = of(Registries.PLACEMENT_MODIFIER_TYPE);
    RegistryView<BlockStateProviderType<?>> BLOCKSTATE_PROVIDER_TYPE = of(Registries.BLOCK_STATE_PROVIDER_TYPE);
    RegistryView<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = of(Registries.FOLIAGE_PLACER_TYPE);
    RegistryView<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = of(Registries.TRUNK_PLACER_TYPE);
    RegistryView<RootPlacerType<?>> ROOT_PLACER_TYPE = of(Registries.ROOT_PLACER_TYPE);
    RegistryView<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = of(Registries.TREE_DECORATOR_TYPE);
    RegistryView<FeatureSizeType<?>> FEATURE_SIZE_TYPE = of(Registries.FEATURE_SIZE_TYPE);
    RegistryView<Codec<? extends BiomeSource>> BIOME_SOURCE = of(Registries.BIOME_SOURCE);
    RegistryView<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR = of(Registries.CHUNK_GENERATOR);
    RegistryView<Codec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITION = of(Registries.MATERIAL_CONDITION);
    RegistryView<Codec<? extends SurfaceRules.RuleSource>> MATERIAL_RULE = of(Registries.MATERIAL_RULE);
    RegistryView<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPE = of(Registries.DENSITY_FUNCTION_TYPE);
    RegistryView<StructureProcessorType<?>> STRUCTURE_PROCESSOR = of(Registries.STRUCTURE_PROCESSOR);
    RegistryView<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = of(Registries.STRUCTURE_POOL_ELEMENT);
    RegistryView<CatVariant> CAT_VARIANT = of(Registries.CAT_VARIANT);
    RegistryView<FrogVariant> FROG_VARIANT = of(Registries.FROG_VARIANT);
    RegistryView<BannerPattern> BANNER_PATTERN = of(Registries.BANNER_PATTERN);
    RegistryView<Instrument> INSTRUMENT = of(Registries.INSTRUMENT);
    RegistryView<String> DECORATED_POT_PATTERNS = of(Registries.DECORATED_POT_PATTERNS);
    RegistryView<CreativeModeTab> CREATIVE_MODE_TABS = of(Registries.CREATIVE_MODE_TAB);

    static <T> RegistryView<T> of(ResourceKey<? extends Registry<T>> key) {
        return RegistryViewImpl.getRegistry(key.location());
    }

    static <T> RegistryView<T> of(ResourceLocation location) {
        return RegistryViewImpl.getRegistry(location);
    }

    static Set<Map.Entry<ResourceLocation, RegistryView<?>>> allRegistries() {
        return RegistryViewImpl.allRegistries();
    }

    /**
     * @return The codec to identify objects of this registry
     */
    Codec<T> byNameCodec();

    /**
     * Retrieves the key for the specified value.
     *
     * @param value The value to get the key for
     * @return A key for that value or <code>null</code> if this registry doesn't contain that value
     */
    @Nullable
    ResourceLocation getKey(T value);

    /**
     * Retrieves the resource key for the specified value.
     *
     * @param value The value to get the key for
     * @return A key for that value or {@link Optional#empty()} if this registry doesn't contain that value
     */
    Optional<ResourceKey<T>> getResourceKey(T value);

    /**
     * Retrieves the id for the specified value. This can only be used for a custom registry.
     *
     * @param value The value to get the id for
     * @return An id for that value or <code>null</code> if this registry doesn't contain that id
     */
    int getId(@Nullable T value);

    /**
     * Retrieves the value for the specified id. This can only be used for a custom registry.
     *
     * @param id The id to get the value for
     * @return A value for that id or <code>null</code> if this registry doesn't contain a value with that id
     */
    @Nullable
    T byId(int id);

    /**
     * @return The size of the registry
     */
    @Override
    int size();

    /**
     * Retrieves the value for the specified key.
     *
     * @param name The key to get the value for
     * @return A value for that key or <code>null</code> if this registry doesn't contain a value with that name
     */
    @Nullable
    T get(@Nullable ResourceLocation name);

    /**
     * Retrieves the value for the specified key.
     *
     * @param name The key to get the value for
     * @return A value for that key
     */
    default Optional<T> getOptional(@Nullable ResourceLocation name) {
        return Optional.ofNullable(this.get(name));
    }

    /**
     * @return The key of this registry
     */
    ResourceKey<? extends Registry<T>> key();

    /**
     * Retrieves the value for the specified id.
     *
     * @param id The id to get the value for
     * @return A value for that id
     */
    default Optional<T> byIdOptional(int id) {
        return Optional.ofNullable(this.byId(id));
    }

    /**
     * @return A set of all registered keys in the registry
     */
    Set<ResourceLocation> keySet();

    /**
     * @return A set of all registered entries in the entry
     */
    Set<Map.Entry<ResourceKey<T>, T>> entrySet();

    /**
     * @return A stream of all values in the registry
     */
    default Stream<T> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    Iterable<T> getTagOrEmpty(TagKey<T> tagKey);

    /**
     * Checks to see if a value with the specified name exists.
     *
     * @param name The name of the key to get
     * @return Whether that value exists
     */
    boolean containsKey(ResourceLocation name);
}

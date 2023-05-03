package com.teamaurora.magnetosphere.api.registry.v1;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.teamaurora.magnetosphere.impl.registry.RegistryViewImpl;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
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

    RegistryView<GameEvent> GAME_EVENT = get(Registries.GAME_EVENT);
    RegistryView<SoundEvent> SOUND_EVENT = get(Registries.SOUND_EVENT);
    RegistryView<Fluid> FLUID = get(Registries.FLUID);
    RegistryView<MobEffect> MOB_EFFECT = get(Registries.MOB_EFFECT);
    RegistryView<Block> BLOCK = get(Registries.BLOCK);
    RegistryView<Enchantment> ENCHANTMENT = get(Registries.ENCHANTMENT);
    RegistryView<EntityType<?>> ENTITY_TYPE = get(Registries.ENTITY_TYPE);
    RegistryView<Item> ITEM = get(Registries.ITEM);
    RegistryView<Potion> POTION = get(Registries.POTION);
    RegistryView<ParticleType<?>> PARTICLE_TYPE = get(Registries.PARTICLE_TYPE);
    RegistryView<BlockEntityType<?>> BLOCK_ENTITY_TYPE = get(Registries.BLOCK_ENTITY_TYPE);
    RegistryView<PaintingVariant> PAINTING_VARIANT = get(Registries.PAINTING_VARIANT);
    RegistryView<ResourceLocation> CUSTOM_STAT = get(Registries.CUSTOM_STAT);
    RegistryView<ChunkStatus> CHUNK_STATUS = get(Registries.CHUNK_STATUS);
    RegistryView<RuleTestType<?>> RULE_TEST = get(Registries.RULE_TEST);
    RegistryView<PosRuleTestType<?>> POS_RULE_TEST = get(Registries.POS_RULE_TEST);
    RegistryView<MenuType<?>> MENU = get(Registries.MENU);
    RegistryView<RecipeType<?>> RECIPE_TYPE = get(Registries.RECIPE_TYPE);
    RegistryView<RecipeSerializer<?>> RECIPE_SERIALIZER = get(Registries.RECIPE_SERIALIZER);
    RegistryView<Attribute> ATTRIBUTE = get(Registries.ATTRIBUTE);
    RegistryView<PositionSourceType<?>> POSITION_SOURCE_TYPE = get(Registries.POSITION_SOURCE_TYPE);
    RegistryView<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPE = get(Registries.COMMAND_ARGUMENT_TYPE);
    RegistryView<StatType<?>> STAT_TYPE = get(Registries.STAT_TYPE);
    RegistryView<VillagerType> VILLAGER_TYPE = get(Registries.VILLAGER_TYPE);
    RegistryView<VillagerProfession> VILLAGER_PROFESSION = get(Registries.VILLAGER_PROFESSION);
    RegistryView<PoiType> POINT_OF_INTEREST_TYPE = get(Registries.POINT_OF_INTEREST_TYPE);
    RegistryView<MemoryModuleType<?>> MEMORY_MODULE_TYPE = get(Registries.MEMORY_MODULE_TYPE);
    RegistryView<SensorType<?>> SENSOR_TYPE = get(Registries.SENSOR_TYPE);
    RegistryView<Schedule> SCHEDULE = get(Registries.SCHEDULE);
    RegistryView<Activity> ACTIVITY = get(Registries.ACTIVITY);
    RegistryView<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = get(Registries.LOOT_POOL_ENTRY_TYPE);
    RegistryView<LootItemFunctionType> LOOT_FUNCTION_TYPE = get(Registries.LOOT_FUNCTION_TYPE);
    RegistryView<LootItemConditionType> LOOT_CONDITION_TYPE = get(Registries.LOOT_CONDITION_TYPE);
    RegistryView<LootNumberProviderType> LOOT_NUMBER_PROVIDER_TYPE = get(Registries.LOOT_NUMBER_PROVIDER_TYPE);
    RegistryView<LootNbtProviderType> LOOT_NBT_PROVIDER_TYPE = get(Registries.LOOT_NBT_PROVIDER_TYPE);
    RegistryView<LootScoreProviderType> LOOT_SCORE_PROVIDER_TYPE = get(Registries.LOOT_SCORE_PROVIDER_TYPE);
    RegistryView<FloatProviderType<?>> FLOAT_PROVIDER_TYPE = get(Registries.FLOAT_PROVIDER_TYPE);
    RegistryView<IntProviderType<?>> INT_PROVIDER_TYPE = get(Registries.INT_PROVIDER_TYPE);
    RegistryView<HeightProviderType<?>> HEIGHT_PROVIDER_TYPE = get(Registries.HEIGHT_PROVIDER_TYPE);
    RegistryView<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = get(Registries.BLOCK_PREDICATE_TYPE);
    RegistryView<WorldCarver<?>> CARVER = get(Registries.CARVER);
    RegistryView<Feature<?>> FEATURE = get(Registries.FEATURE);
    RegistryView<StructurePlacementType<?>> STRUCTURE_PLACEMENT = get(Registries.STRUCTURE_PLACEMENT);
    RegistryView<StructurePieceType> STRUCTURE_PIECE = get(Registries.STRUCTURE_PIECE);
    RegistryView<StructureType<?>> STRUCTURE_TYPE = get(Registries.STRUCTURE_TYPE);
    RegistryView<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = get(Registries.PLACEMENT_MODIFIER_TYPE);
    RegistryView<BlockStateProviderType<?>> BLOCKSTATE_PROVIDER_TYPE = get(Registries.BLOCK_STATE_PROVIDER_TYPE);
    RegistryView<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = get(Registries.FOLIAGE_PLACER_TYPE);
    RegistryView<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = get(Registries.TRUNK_PLACER_TYPE);
    RegistryView<RootPlacerType<?>> ROOT_PLACER_TYPE = get(Registries.ROOT_PLACER_TYPE);
    RegistryView<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = get(Registries.TREE_DECORATOR_TYPE);
    RegistryView<FeatureSizeType<?>> FEATURE_SIZE_TYPE = get(Registries.FEATURE_SIZE_TYPE);
    RegistryView<Codec<? extends BiomeSource>> BIOME_SOURCE = get(Registries.BIOME_SOURCE);
    RegistryView<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR = get(Registries.CHUNK_GENERATOR);
    RegistryView<Codec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITION = get(Registries.MATERIAL_CONDITION);
    RegistryView<Codec<? extends SurfaceRules.RuleSource>> MATERIAL_RULE = get(Registries.MATERIAL_RULE);
    RegistryView<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPE = get(Registries.DENSITY_FUNCTION_TYPE);
    RegistryView<StructureProcessorType<?>> STRUCTURE_PROCESSOR = get(Registries.STRUCTURE_PROCESSOR);
    RegistryView<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = get(Registries.STRUCTURE_POOL_ELEMENT);
    RegistryView<CatVariant> CAT_VARIANT = get(Registries.CAT_VARIANT);
    RegistryView<FrogVariant> FROG_VARIANT = get(Registries.FROG_VARIANT);
    RegistryView<BannerPattern> BANNER_PATTERN = get(Registries.BANNER_PATTERN);
    RegistryView<Instrument> INSTRUMENT = get(Registries.INSTRUMENT);
    RegistryView<String> DECORATED_POT_PATTERNS = get(Registries.DECORATED_POT_PATTERNS);

    static <T> RegistryView<T> get(ResourceKey<? extends Registry<T>> key) {
        return RegistryViewImpl.getRegistry(key.location());
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

    /**
     * Checks to see if a value with the specified name exists.
     *
     * @param name The name of the key to get
     * @return Whether that value exists
     */
    boolean containsKey(ResourceLocation name);
}

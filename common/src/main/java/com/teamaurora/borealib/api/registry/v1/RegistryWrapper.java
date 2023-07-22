package com.teamaurora.borealib.api.registry.v1;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.teamaurora.borealib.impl.registry.RegistryWrapperImpl;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
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
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * Provides a read-only view of platform specific registries.
 *
 * @param <T> The top level of the registry type
 */
@ApiStatus.NonExtendable
public interface RegistryWrapper<T> extends Keyable, IdMap<T> {

    RegistryWrapper<Block> BLOCK = get(Registries.BLOCK);
    RegistryWrapper<Enchantment> ENCHANTMENT = get(Registries.ENCHANTMENT);
    RegistryWrapper<EntityType<?>> ENTITY_TYPE = get(Registries.ENTITY_TYPE);
    RegistryWrapper<Item> ITEM = get(Registries.ITEM);
    RegistryWrapper<Potion> POTION = get(Registries.POTION);
    RegistryWrapper<ParticleType<?>> PARTICLE_TYPE = get(Registries.PARTICLE_TYPE);
    RegistryWrapper<BlockEntityType<?>> BLOCK_ENTITY_TYPE = get(Registries.BLOCK_ENTITY_TYPE);
    RegistryWrapper<PaintingVariant> PAINTING_VARIANT = get(Registries.PAINTING_VARIANT);
    RegistryWrapper<RecipeType<?>> RECIPE_TYPE = get(Registries.RECIPE_TYPE);
    RegistryWrapper<RecipeSerializer<?>> RECIPE_SERIALIZER = get(Registries.RECIPE_SERIALIZER);
    RegistryWrapper<Attribute> ATTRIBUTE = get(Registries.ATTRIBUTE);
    RegistryWrapper<StatType<?>> STAT_TYPE = get(Registries.STAT_TYPE);
    RegistryWrapper<VillagerType> VILLAGER_TYPE = get(Registries.VILLAGER_TYPE);
    RegistryWrapper<PoiType> POINT_OF_INTEREST_TYPE = get(Registries.POINT_OF_INTEREST_TYPE);
    RegistryWrapper<MemoryModuleType<?>> MEMORY_MODULE_TYPE = get(Registries.MEMORY_MODULE_TYPE);
    RegistryWrapper<SensorType<?>> SENSOR_TYPE = get(Registries.SENSOR_TYPE);
    RegistryWrapper<Schedule> SCHEDULE = get(Registries.SCHEDULE);
    RegistryWrapper<Activity> ACTIVITY = get(Registries.ACTIVITY);
    RegistryWrapper<WorldCarver<?>> CARVER = get(Registries.CARVER);
    RegistryWrapper<Feature<?>> FEATURE = get(Registries.FEATURE);
    RegistryWrapper<StructurePlacementType<?>> STRUCTURE_PLACEMENT = get(Registries.STRUCTURE_PLACEMENT);
    RegistryWrapper<StructurePieceType> STRUCTURE_PIECE = get(Registries.STRUCTURE_PIECE);
    RegistryWrapper<StructureType<?>> STRUCTURE_TYPE = get(Registries.STRUCTURE_TYPE);
    RegistryWrapper<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = get(Registries.FOLIAGE_PLACER_TYPE);
    RegistryWrapper<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = get(Registries.TRUNK_PLACER_TYPE);
    RegistryWrapper<RootPlacerType<?>> ROOT_PLACER_TYPE = get(Registries.ROOT_PLACER_TYPE);
    RegistryWrapper<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = get(Registries.TREE_DECORATOR_TYPE);
    RegistryWrapper<Codec<? extends BiomeSource>> BIOME_SOURCE = get(Registries.BIOME_SOURCE);
    RegistryWrapper<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR = get(Registries.CHUNK_GENERATOR);
    RegistryWrapper<Codec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITION = get(Registries.MATERIAL_CONDITION);
    RegistryWrapper<Codec<? extends SurfaceRules.RuleSource>> MATERIAL_RULE = get(Registries.MATERIAL_RULE);
    RegistryWrapper<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPE = get(Registries.DENSITY_FUNCTION_TYPE);
    RegistryWrapper<StructureProcessorType<?>> STRUCTURE_PROCESSOR = get(Registries.STRUCTURE_PROCESSOR);
    RegistryWrapper<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = get(Registries.STRUCTURE_POOL_ELEMENT);
    RegistryWrapper<BannerPattern> BANNER_PATTERN = get(Registries.BANNER_PATTERN);
    RegistryWrapper<Instrument> INSTRUMENT = get(Registries.INSTRUMENT);
    RegistryWrapper<CreativeModeTab> CREATIVE_MODE_TABS = get(Registries.CREATIVE_MODE_TAB);
    RegistryWrapper<VillagerProfession> VILLAGER_PROFESSION = get(Registries.VILLAGER_PROFESSION);

    /**
     * Gets or creates a registry provider for the owning mod. Only one provider for a given registry exists for each mod.
     *
     * @param key   The registry to get a provider for
     * @param owner The id of the mod the provider is for (this doesn't have to be the namespace objects are registered under)
     * @param <T>   The top level registry type
     * @return A provider for the registry
     */
    static <T> RegistryWrapper.Provider<T> provider(ResourceKey<? extends Registry<T>> key, String owner) {
        return RegistryWrapperImpl.provider(key, owner);
    }

    /**
     * Gets a {@link RegistryWrapper} for the specified registry key.
     * <p>Corresponding wrappers to platform registries are created as needed and aren't present if this method isn't used.
     *
     * @param key The key of the registry
     * @param <T> The top level registry type
     * @return The registry if it exists
     */
    static <T> RegistryWrapper<T> get(ResourceKey<? extends Registry<T>> key) {
        return RegistryWrapperImpl.getRegistry(key.location());
    }

    /**
     * Gets a {@link RegistryWrapper} for the specified registry id.
     * <p>Corresponding wrappers to platform registries are created as needed and aren't present if this method isn't used.
     *
     * @param id  The id of the registry
     * @param <T> The top level registry type
     * @return The registry if it exists
     */
    static <T> RegistryWrapper<T> getById(ResourceLocation id) {
        return RegistryWrapperImpl.getRegistry(id);
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

    /**
     * Used to queue content to be added to registries.
     *
     * @param <T> The top level registry type
     */
    interface Provider<T> extends Iterable<RegistryReference<T>> {

        /**
         * @return The id of the mod controlling the provider; this may or may not be the namespace of the registered objects
         */
        String owner();

        /**
         * @return The registry key the provider is adding to
         */
        ResourceKey<? extends Registry<T>> getRegistryKey();

        /**
         * Adds an object to be registered.
         *
         * @param name   The name of the object to register
         * @param object A supplier creating the object to register
         * @param <R>    The object type
         * @return A reference pointing to the registered object when it's ready
         */
        <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object);

        /**
         * Adds an object to be registered under the same namespace as the controlling mod.
         *
         * @param path   The path of the object to register
         * @param object A supplier creating the object to register
         * @param <R>    The object type
         * @return A reference pointing to the registered object when it's ready
         */
        default <R extends T> RegistryReference<R> register(String path, Supplier<? extends R> object) {
            return this.register(new ResourceLocation(this.owner(), path), object);
        }

        /**
         * @return A stream of all added references
         */
        default Stream<RegistryReference<T>> stream() {
            return StreamSupport.stream(this.spliterator(), false);
        }

        // Called internally to ensure registry classes are initialized
        @ApiStatus.Internal
        default void register() {}
    }
}

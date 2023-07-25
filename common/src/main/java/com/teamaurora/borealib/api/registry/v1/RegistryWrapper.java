package com.teamaurora.borealib.api.registry.v1;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.Lifecycle;
import com.teamaurora.borealib.api.registry.v1.extended.FeatureRegistryProvider;
import com.teamaurora.borealib.impl.registry.RegistryWrapperImpl;
import net.minecraft.core.IdMap;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;
import net.minecraft.world.item.BlockItem;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * An abstracted registry with helper methods to assist in accessing Fabric and Forge registry apis.
 *
 * @param <T> The top level of the registry type
 * @author ebo2022
 * @since 1.0
 */
@ApiStatus.NonExtendable
public interface RegistryWrapper<T> extends Keyable, IdMap<T> {

    RegistryWrapper<Block> BLOCKS = get(Registries.BLOCK);
    RegistryWrapper<Enchantment> ENCHANTMENTS = get(Registries.ENCHANTMENT);
    RegistryWrapper<EntityType<?>> ENTITY_TYPES = get(Registries.ENTITY_TYPE);
    RegistryWrapper<Item> ITEMS = get(Registries.ITEM);
    RegistryWrapper<Potion> POTIONS = get(Registries.POTION);
    RegistryWrapper<ParticleType<?>> PARTICLE_TYPES = get(Registries.PARTICLE_TYPE);
    RegistryWrapper<BlockEntityType<?>> BLOCK_ENTITY_TYPES = get(Registries.BLOCK_ENTITY_TYPE);
    RegistryWrapper<PaintingVariant> PAINTING_VARIANTS = get(Registries.PAINTING_VARIANT);
    RegistryWrapper<RecipeType<?>> RECIPE_TYPES = get(Registries.RECIPE_TYPE);
    RegistryWrapper<RecipeSerializer<?>> RECIPE_SERIALIZERS = get(Registries.RECIPE_SERIALIZER);
    RegistryWrapper<Attribute> ATTRIBUTES = get(Registries.ATTRIBUTE);
    RegistryWrapper<StatType<?>> STAT_TYPES = get(Registries.STAT_TYPE);
    RegistryWrapper<VillagerType> VILLAGER_TYPES = get(Registries.VILLAGER_TYPE);
    RegistryWrapper<PoiType> POINT_OF_INTEREST_TYPES = get(Registries.POINT_OF_INTEREST_TYPE);
    RegistryWrapper<MemoryModuleType<?>> MEMORY_MODULE_TYPES = get(Registries.MEMORY_MODULE_TYPE);
    RegistryWrapper<SensorType<?>> SENSOR_TYPES = get(Registries.SENSOR_TYPE);
    RegistryWrapper<Schedule> SCHEDULES = get(Registries.SCHEDULE);
    RegistryWrapper<Activity> ACTIVITIES = get(Registries.ACTIVITY);
    RegistryWrapper<WorldCarver<?>> CARVERS = get(Registries.CARVER);
    RegistryWrapper<Feature<?>> FEATURES = get(Registries.FEATURE);
    RegistryWrapper<StructurePlacementType<?>> STRUCTURE_PLACEMENTS = get(Registries.STRUCTURE_PLACEMENT);
    RegistryWrapper<StructurePieceType> STRUCTURE_PIECES = get(Registries.STRUCTURE_PIECE);
    RegistryWrapper<StructureType<?>> STRUCTURE_TYPES = get(Registries.STRUCTURE_TYPE);
    RegistryWrapper<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = get(Registries.FOLIAGE_PLACER_TYPE);
    RegistryWrapper<TrunkPlacerType<?>> TRUNK_PLACER_TYPES = get(Registries.TRUNK_PLACER_TYPE);
    RegistryWrapper<RootPlacerType<?>> ROOT_PLACER_TYPES = get(Registries.ROOT_PLACER_TYPE);
    RegistryWrapper<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = get(Registries.TREE_DECORATOR_TYPE);
    RegistryWrapper<Codec<? extends BiomeSource>> BIOME_SOURCES = get(Registries.BIOME_SOURCE);
    RegistryWrapper<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS = get(Registries.CHUNK_GENERATOR);
    RegistryWrapper<Codec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITIONS = get(Registries.MATERIAL_CONDITION);
    RegistryWrapper<Codec<? extends SurfaceRules.RuleSource>> MATERIAL_RULES = get(Registries.MATERIAL_RULE);
    RegistryWrapper<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = get(Registries.DENSITY_FUNCTION_TYPE);
    RegistryWrapper<StructureProcessorType<?>> STRUCTURE_PROCESSORS = get(Registries.STRUCTURE_PROCESSOR);
    RegistryWrapper<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENTS = get(Registries.STRUCTURE_POOL_ELEMENT);
    RegistryWrapper<BannerPattern> BANNER_PATTERNS = get(Registries.BANNER_PATTERN);
    RegistryWrapper<Instrument> INSTRUMENTS = get(Registries.INSTRUMENT);
    RegistryWrapper<CreativeModeTab> CREATIVE_MODE_TABS = get(Registries.CREATIVE_MODE_TAB);
    RegistryWrapper<VillagerProfession> VILLAGER_PROFESSIONS = get(Registries.VILLAGER_PROFESSION);

    /**
     * Gets a {@link RegistryWrapper} for the specified registry key.
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
     *
     * @param id  The id of the registry
     * @param <T> The top level registry type
     * @return The registry if it exists
     */
    static <T> RegistryWrapper<T> getById(ResourceLocation id) {
        return RegistryWrapperImpl.getRegistry(id);
    }

    /**
     * Creates a simple custom {@link RegistryWrapper} for general usage.
     *
     * @param id  The name of the registry
     * @param <T> The top level registry type
     * @return A new {@link SimpleCustomRegistry}
     */
    static <T> SimpleCustomRegistry<T> createSimple(ResourceLocation id) {
        return new SimpleCustomRegistry<>(new MappedRegistry<>(ResourceKey.createRegistryKey(id), Lifecycle.stable()));
    }

    /**
     * Creates a world-specific dynamic registry that loads its contents from JSON.
     * <p>Objects will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry.
     * <p>The resultant registry is found in the server or client <code>RegistryAccess</code> (if there is a specified network codec).
     *
     * @param id           The name of the registry
     * @param codec        A codec to (de)serialize registry entries from JSON
     * @param networkCodec An optional codec to sync registry contents to clients
     * @param <T>          The registry object type
     * @return The key to find registry contents in {@link RegistryAccess}
     */
    static <T> ResourceKey<? extends Registry<T>> dynamicRegistry(ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        return RegistryWrapperImpl.dynamicRegistry(id, codec, networkCodec);
    }

    /**
     * Creates a world-specific dynamic registry that loads its contents from JSON.
     * <p>Objects will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry.
     * <p>The resultant registry is found in the server <code>RegistryAccess</code>.
     *
     * @param id    The name of the registry
     * @param codec A codec to (de)serialize registry entries from JSON
     * @param <T>   The registry object type
     * @return The key to find registry contents in {@link RegistryAccess}
     */
    static <T> ResourceKey<? extends Registry<T>> dynamicRegistry(ResourceLocation id, Codec<T> codec) {
        return dynamicRegistry(id, codec, null);
    }

    /**
     * Creates a registry provider with the specified id.
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
     * Creates a specialized {@link BlockEntityProvider} with the specified id.
     *
     * @param owner The id of the mod the provider is for (this doesn't have to be the namespace objects are registered under)
     * @return A specialized block entity provider
     */
    static BlockEntityProvider blockEntityProvider(String owner) {
        return new BlockEntityProvider(provider(Registries.BLOCK_ENTITY_TYPE, owner));
    }

    /**
     * Creates a specialized {@link BlockProvider} with the specified id.
     *
     * @param itemProvider The provider to use to register items
     * @return A specialized block provider to register block items
     */
    static BlockProvider blockProvider(Provider<Item> itemProvider) {
        return new BlockProvider(provider(Registries.BLOCK, itemProvider.owner()), itemProvider);
    }

    /**
     * Creates a specialized {@link EntityProvider} with the specified id.
     *
     * @param owner The id of the mod the provider is for (this doesn't have to be the namespace objects are registered under)
     * @return A specialized entity provider
     */
    static EntityProvider entityProvider(String owner) {
        return new EntityProvider(provider(Registries.ENTITY_TYPE, owner));
    }

    /**
     * Creates a specialized {@link FeatureProvider} with the specified id.
     *
     * @param owner The id of the mod the provider is for (this doesn't have to be the namespace objects are registered under)
     * @return A specialized feature provider
     */
    static FeatureProvider featureProvider(String owner) {
        return new FeatureProvider(provider(Registries.FEATURE, owner));
    }

    /**
     * @return The codec to identify objects from this registry
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

        /**
         * Optional but recommended to ensure the registry class is loaded.
         */
        default void register() {}
    }

    class DelegatedProvider<T> implements RegistryWrapper.Provider<T> {

        private final Provider<T> parent;

        protected DelegatedProvider(RegistryWrapper.Provider<T> parent) {
            this.parent = parent;
        }

        @Override
        public String owner() {
            return this.parent.owner();
        }

        @Override
        public ResourceKey<? extends Registry<T>> getRegistryKey() {
            return this.parent.getRegistryKey();
        }

        @Override
        public <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object) {
            return this.parent.register(name, object);
        }

        @NotNull
        @Override
        public Iterator<RegistryReference<T>> iterator() {
            return this.parent.iterator();
        }
    }

    /**
     * A specialized provider to help with {@link BlockEntityType}s.
     *
     * @since 1.0
     */
    final class BlockEntityProvider extends DelegatedProvider<BlockEntityType<?>> {

        private BlockEntityProvider(RegistryWrapper.Provider<BlockEntityType<?>> parent) {
            super(parent);
        }

        private static Supplier<Set<Block>> collectBlocks(Class<?> clazz) {
            return Suppliers.memoize(() -> BLOCKS.stream().filter(clazz::isInstance).collect(Collectors.toSet()));
        }

        /**
         * Registers a block entity with lazily supplied blocks rather than having them defined in the registry.
         *
         * @param name         The name of the block entity
         * @param factory      The factory to generate the block entity
         * @param lazyBlockSet A supplier for the blocks that this entity should support
         * @param <T>          The block entity type
         * @return A reference to the registered block entity
         */
        public <T extends BlockEntity> RegistryReference<BlockEntityType<T>> registerLazy(ResourceLocation name, BlockEntityType.BlockEntitySupplier<? extends T> factory, Supplier<Set<Block>> lazyBlockSet) {
            return this.register(name, () -> new BlockEntityType<>(factory, Collections.emptySet(), null) {

                @Override
                public boolean isValid(BlockState blockState) {
                    return lazyBlockSet.get().contains(blockState.getBlock());
                }
            });
        }

        /**
         * Registers a block entity that has all blocks in the specified class as part of it.
         *
         * @param name    The name of the block entity
         * @param factory The factory to generate the block entity
         * @param clazz   The class of blocks that should be included in the block entity
         * @param <T>     The block entity type
         * @return A reference to the registered block entity
         */
        public <T extends BlockEntity> RegistryReference<BlockEntityType<T>> registerTyped(ResourceLocation name, BlockEntityType.BlockEntitySupplier<? extends T> factory, Class<?> clazz) {
            return this.registerLazy(name, factory, collectBlocks(clazz));
        }

        /**
         * Registers a block entity with lazily supplied blocks rather than having them defined in the registry.
         *
         * @param path         The name of the block entity
         * @param factory      The factory to generate the block entity
         * @param lazyBlockSet A supplier for the blocks that this entity should support
         * @param <T>          The block entity type
         * @return A reference to the registered block entity
         */
        public <T extends BlockEntity> RegistryReference<BlockEntityType<T>> registerLazy(String path, BlockEntityType.BlockEntitySupplier<? extends T> factory, Supplier<Set<Block>> lazyBlockSet) {
            return this.registerLazy(new ResourceLocation(this.owner(), path), factory, lazyBlockSet);
        }

        /**
         * Registers a block entity that has all blocks in the specified class as part of it.
         *
         * @param path    The name of the block entity
         * @param factory The factory to generate the block entity
         * @param clazz   The class of blocks that should be included in the block entity
         * @param <T>     The block entity type
         * @return A reference to the registered block entity
         */
        public <T extends BlockEntity> RegistryReference<BlockEntityType<T>> registerTyped(String path, BlockEntityType.BlockEntitySupplier<? extends T> factory, Class<? extends Block> clazz) {
            return this.registerLazy(path, factory, collectBlocks(clazz));
        }
    }

    /**
     * A specialized provider for {@link Block}s to help with block items and unique blocks.
     *
     * @since 1.0
     */
    final class BlockProvider extends DelegatedProvider<Block> {

        private final RegistryWrapper.Provider<Item> itemProvider;

        private BlockProvider(RegistryWrapper.Provider<Block> blockRegistry, RegistryWrapper.Provider<Item> itemProvider) {
            super(blockRegistry);
            this.itemProvider = itemProvider;
        }

        /**
         * Registers a block with a regular {@link BlockItem}.
         *
         * @param path       The name of the block
         * @param block      A supplier of the block to register
         * @param properties The properties to use for the block item
         * @param <R>        The block type
         * @return A reference to the registered block
         */
        public <R extends Block> RegistryReference<R> registerWithItem(String path, Supplier<? extends R> block, Item.Properties properties) {
            return this.registerWithItem(path, block, object -> new BlockItem(object, properties));
        }

        /**
         * Registers a block with a custom {@link BlockItem}.
         *
         * @param path        The name of the block
         * @param block       A supplier of the block to register
         * @param itemFactory The factory to generate the custom block item
         * @param <R>         The block type
         * @return A reference to the registered block
         */
        public <R extends Block> RegistryReference<R> registerWithItem(String path, Supplier<? extends R> block, Function<R, Item> itemFactory) {
            RegistryReference<R> register = this.register(path, block);
            this.itemProvider.register(path, () -> itemFactory.apply(register.get()));
            return register;
        }

        /**
         * Registers a block with a regular {@link BlockItem}.
         *
         * @param name       The name of the block
         * @param block      A supplier of the block to register
         * @param properties The properties to use for the block item
         * @param <R>        The block type
         * @return A reference to the registered block
         */
        public <R extends Block> RegistryReference<R> registerWithItem(ResourceLocation name, Supplier<? extends R> block, Item.Properties properties) {
            return this.registerWithItem(name, block, object -> new BlockItem(object, properties));
        }

        /**
         * Registers a block with a custom {@link BlockItem}.
         *
         * @param name        The name of the block
         * @param block       A supplier of the block to register
         * @param itemFactory The factory to generate the custom block item
         * @param <R>         The block type
         * @return A reference to the registered block
         */
        public <R extends Block> RegistryReference<R> registerWithItem(ResourceLocation name, Supplier<? extends R> block, Function<R, Item> itemFactory) {
            RegistryReference<R> register = this.register(name, block);
            this.itemProvider.register(name, () -> itemFactory.apply(register.get()));
            return register;
        }

        /**
         * @return The provider used to register items and block items
         */
        public RegistryWrapper.Provider<Item> getItemProvider() {
            return this.itemProvider;
        }
    }

    /**
     * A specialized provider for entities, alongside AI such as activities and schedules.
     *
     * @since 1.0
     */
    final class EntityProvider extends DelegatedProvider<EntityType<?>> {

        private final RegistryWrapper.Provider<MemoryModuleType<?>> memoryModuleTypeProvider;
        private final RegistryWrapper.Provider<SensorType<?>> sensorTypeProvider;
        private final RegistryWrapper.Provider<Schedule> scheduleProvider;
        private final RegistryWrapper.Provider<Activity> activityProvider;

        private EntityProvider(RegistryWrapper.Provider<EntityType<?>> entityRegistry) {
            super(entityRegistry);
            this.memoryModuleTypeProvider = RegistryWrapper.provider(Registries.MEMORY_MODULE_TYPE, entityRegistry.owner());
            this.sensorTypeProvider = RegistryWrapper.provider(Registries.SENSOR_TYPE, entityRegistry.owner());
            this.scheduleProvider = RegistryWrapper.provider(Registries.SCHEDULE, entityRegistry.owner());
            this.activityProvider = RegistryWrapper.provider(Registries.ACTIVITY, entityRegistry.owner());
        }

        /**
         * Registers a new activity.
         *
         * @param path The name of the activity
         * @return A reference to the registered activity
         */
        public RegistryReference<Activity> registerActivity(String path) {
            return this.activityProvider.register(path, () -> new Activity(this.activityProvider.owner() + ":" + path));
        }

        /**
         * Registers a new memory module type.
         *
         * @param path The name of the memory module type
         * @param <R>  The type of memory module
         * @return A reference to the registered memory module type
         */
        public <R> RegistryReference<MemoryModuleType<R>> registerMemoryModuleType(String path) {
            return this.registerMemoryModuleType(path, null);
        }

        /**
         * Registers a new memory module type with a {@link Codec} to help serialize.
         *
         * @param path  The name of the memory module type
         * @param codec The codec to use
         * @param <R>   The type of memory module
         * @return A reference to the registered memory module type
         */
        public <R> RegistryReference<MemoryModuleType<R>> registerMemoryModuleType(String path, @Nullable Codec<R> codec) {
            return this.memoryModuleTypeProvider.register(path, () -> new MemoryModuleType<>(Optional.ofNullable(codec)));
        }

        /**
         * Registers a new sensor type.
         *
         * @param path     The name of the sensor type
         * @param supplier A supplier to create the sensor type
         * @param <R>      The type of sensor
         * @return A reference to the registered sensor type
         */
        public <R extends Sensor<?>> RegistryReference<SensorType<R>> registerSensorType(String path, Supplier<R> supplier) {
            return this.sensorTypeProvider.register(path, () -> new SensorType<>(supplier));
        }

        /**
         * Registers a new {@link Schedule} from a schedule builder.
         *
         * @param path    The name of the schedule
         * @param builder A consumer to build the schedule
         * @return A reference to the registered schedule
         */
        public RegistryReference<Schedule> registerSchedule(String path, Consumer<ScheduleBuilder> builder) {
            return this.scheduleProvider.register(path, () -> {
                Schedule schedule = new Schedule();
                builder.accept(new ScheduleBuilder(schedule));
                return schedule;
            });
        }

        /**
         * Registers a new activity.
         *
         * @param name The name of the activity
         * @return A reference to the registered activity
         */
        public RegistryReference<Activity> registerActivity(ResourceLocation name) {
            return this.activityProvider.register(name, () -> new Activity(name.getNamespace() + ":" + name));
        }

        /**
         * Registers a new memory module type.
         *
         * @param name The name of the memory module type
         * @param <R>  The type of memory module
         * @return A reference to the registered memory module type
         */
        public <R> RegistryReference<MemoryModuleType<R>> registerMemoryModuleType(ResourceLocation name) {
            return this.registerMemoryModuleType(name, null);
        }

        /**
         * Registers a new memory module type with a {@link Codec} to help serialize.
         *
         * @param name  The name of the memory module type
         * @param codec The codec to use
         * @param <R>   The type of memory module
         * @return A reference to the registered memory module type
         */
        public <R> RegistryReference<MemoryModuleType<R>> registerMemoryModuleType(ResourceLocation name, @Nullable Codec<R> codec) {
            return this.memoryModuleTypeProvider.register(name, () -> new MemoryModuleType<>(Optional.ofNullable(codec)));
        }

        /**
         * Registers a new sensor type.
         *
         * @param name     The name of the sensor type
         * @param supplier A supplier to create the sensor type
         * @param <R>      The type of sensor
         * @return A reference to the registered sensor type
         */
        public <R extends Sensor<?>> RegistryReference<SensorType<R>> registerSensorType(ResourceLocation name, Supplier<R> supplier) {
            return this.sensorTypeProvider.register(name, () -> new SensorType<>(supplier));
        }

        /**
         * Registers a new {@link Schedule} from a schedule builder.
         *
         * @param name    The name of the schedule
         * @param builder A consumer to build the schedule
         * @return A reference to the registered schedule
         */
        public RegistryReference<Schedule> registerSchedule(ResourceLocation name, Consumer<ScheduleBuilder> builder) {
            return this.scheduleProvider.register(name, () -> {
                Schedule schedule = new Schedule();
                builder.accept(new ScheduleBuilder(schedule));
                return schedule;
            });
        }

        /**
         * @return The provider used to register memory module types
         */
        public RegistryWrapper.Provider<MemoryModuleType<?>> getMemoryModuleTypeProvider() {
            return this.memoryModuleTypeProvider;
        }

        /**
         * @return The provider used to register sensor types
         */
        public RegistryWrapper.Provider<SensorType<?>> getSensorTypeProvider() {
            return this.sensorTypeProvider;
        }

        /**
         * @return The provider used to register schedules
         */
        public RegistryWrapper.Provider<Schedule> getScheduleProvider() {
            return this.scheduleProvider;
        }

        /**
         * @return The provider used to register activities
         */
        public RegistryWrapper.Provider<Activity> getActivityProvider() {
            return this.activityProvider;
        }
    }

    /**
     * A specialized provider to register features and normally inaccessible decorators and placer types.
     *
     * @since 1.0
     */
    final class FeatureProvider extends DelegatedProvider<Feature<?>> {

        private final RegistryWrapper.Provider<FoliagePlacerType<?>> foliagePlacerTypeProvider;
        private final RegistryWrapper.Provider<TrunkPlacerType<?>> trunkPlacerTypeProvider;
        private final RegistryWrapper.Provider<RootPlacerType<?>> rootPlacerTypeProvider;
        private final RegistryWrapper.Provider<TreeDecoratorType<?>> treeDecoratorTypeProvider;

        private FeatureProvider(RegistryWrapper.Provider<Feature<?>> parent) {
            super(parent);
            this.foliagePlacerTypeProvider = RegistryWrapper.provider(Registries.FOLIAGE_PLACER_TYPE, parent.owner());
            this.trunkPlacerTypeProvider = RegistryWrapper.provider(Registries.TRUNK_PLACER_TYPE, parent.owner());
            this.rootPlacerTypeProvider = RegistryWrapper.provider(Registries.ROOT_PLACER_TYPE, parent.owner());
            this.treeDecoratorTypeProvider = RegistryWrapper.provider(Registries.TREE_DECORATOR_TYPE, parent.owner());
        }

        /**
         * Registers a new {@link FoliagePlacerType} with the specified {@link Codec}.
         *
         * @param path  The name of the foliage placer type
         * @param codec A codec to serialize the foliage placers
         * @param <I>   The type of foliage placer
         * @return A reference to the registered foliage placer type
         */
        public <I extends FoliagePlacer> RegistryReference<FoliagePlacerType<I>> registerFoliagePlacerType(String path, Codec<I> codec) {
            return this.foliagePlacerTypeProvider.register(path, () -> new FoliagePlacerType<>(codec));
        }

        /**
         * Registers a new {@link FoliagePlacerType} with the specified {@link Codec}.
         *
         * @param name  The name of the foliage placer type
         * @param codec A codec to serialize the foliage placers
         * @param <I>   The type of foliage placer
         * @return A reference to the registered foliage placer type
         */
        public <I extends FoliagePlacer> RegistryReference<FoliagePlacerType<I>> registerFoliagePlacerType(ResourceLocation name, Codec<I> codec) {
            return this.foliagePlacerTypeProvider.register(name, () -> new FoliagePlacerType<>(codec));
        }

        /**
         * Registers a new {@link TrunkPlacerType} with the specified {@link Codec}.
         *
         * @param path  The name of the trunk placer type
         * @param codec A codec to serialize the trunk placers
         * @param <I>   The type of trunk placer
         * @return A reference to the registered trunk placer type
         */
        public <I extends TrunkPlacer> RegistryReference<TrunkPlacerType<I>> registerTrunkPlacerType(String path, Codec<I> codec) {
            return this.trunkPlacerTypeProvider.register(path, () -> new TrunkPlacerType<>(codec));
        }

        /**
         * Registers a new {@link TrunkPlacerType} with the specified {@link Codec}.
         *
         * @param name  The name of the trunk placer type
         * @param codec A codec to serialize the trunk placers
         * @param <I>   The type of trunk placer
         * @return A reference to the registered trunk placer type
         */
        public <I extends TrunkPlacer> RegistryReference<TrunkPlacerType<I>> registerTrunkPlacerType(ResourceLocation name, Codec<I> codec) {
            return this.trunkPlacerTypeProvider.register(name, () -> new TrunkPlacerType<>(codec));
        }

        /**
         * Registers a new {@link RootPlacerType} with the specified {@link Codec}.
         *
         * @param path  The name of the root placer type
         * @param codec A codec to serialize the root placers
         * @param <I>   The type of root placer
         * @return A reference to the registered root placer type
         */
        public <I extends RootPlacer> RegistryReference<RootPlacerType<I>> registerRootPlacerType(String path, Codec<I> codec) {
            return this.rootPlacerTypeProvider.register(path, () -> new RootPlacerType<>(codec));
        }

        /**
         * Registers a new {@link RootPlacerType} with the specified {@link Codec}.
         *
         * @param name  The name of the root placer type
         * @param codec A codec to serialize the root placers
         * @param <I>   The type of root placer
         * @return A reference to the registered root placer type
         */
        public <I extends RootPlacer> RegistryReference<RootPlacerType<I>> registerRootPlacerType(ResourceLocation name, Codec<I> codec) {
            return this.rootPlacerTypeProvider.register(name, () -> new RootPlacerType<>(codec));
        }

        /**
         * Registers a new {@link TreeDecoratorType} with the specified {@link Codec}.
         *
         * @param path  The name of the tree decorator type
         * @param codec A codec to serialize the tree decorators
         * @param <I>   The type of tree decorator
         * @return A reference to the registered tree decorator type
         */
        public <I extends TreeDecorator> RegistryReference<TreeDecoratorType<I>> registerTreeDecoratorType(String path, Codec<I> codec) {
            return this.treeDecoratorTypeProvider.register(path, () -> new TreeDecoratorType<>(codec));
        }

        /**
         * Registers a new {@link TreeDecoratorType} with the specified {@link Codec}.
         *
         * @param name  The name of the tree decorator type
         * @param codec A codec to serialize the tree decorators
         * @param <I>   The type of tree decorator
         * @return A reference to the registered tree decorator type
         */
        public <I extends TreeDecorator> RegistryReference<TreeDecoratorType<I>> registerTreeDecoratorType(ResourceLocation name, Codec<I> codec) {
            return this.treeDecoratorTypeProvider.register(name, () -> new TreeDecoratorType<>(codec));
        }

        /**
         * @return The provider used to register foliage placer types
         */
        public RegistryWrapper.Provider<FoliagePlacerType<?>> getFoliagePlacerTypeProvider() {
            return this.foliagePlacerTypeProvider;
        }

        /**
         * @return The provider used to register root placer types
         */
        public RegistryWrapper.Provider<RootPlacerType<?>> getRootPlacerTypeProvider() {
            return this.rootPlacerTypeProvider;
        }

        /**
         * @return The provider used to register tree decorator types
         */
        public RegistryWrapper.Provider<TreeDecoratorType<?>> getTreeDecoratorTypeProvider() {
            return this.treeDecoratorTypeProvider;
        }

        /**
         * @return The provider used to register trunk placer types
         */
        public RegistryWrapper.Provider<TrunkPlacerType<?>> getTrunkPlacerTypeProvider() {
            return this.trunkPlacerTypeProvider;
        }
    }

}

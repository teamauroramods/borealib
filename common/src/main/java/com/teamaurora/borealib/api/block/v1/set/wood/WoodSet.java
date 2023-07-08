package com.teamaurora.borealib.api.block.v1.set.wood;

import com.google.common.collect.ImmutableList;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.block.v1.compat.ChestVariant;
import com.teamaurora.borealib.api.block.v1.compat.CommonCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.set.BlockSet;
import com.teamaurora.borealib.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.borealib.api.block.v1.set.variant.ItemVariant;
import com.teamaurora.borealib.api.entity.v1.CustomBoatType;
import com.teamaurora.borealib.api.event.creativetabs.v1.CreativeTabEvents;
import com.teamaurora.borealib.api.registry.v1.DeferredRegister;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import com.teamaurora.borealib.impl.block.set.wood.WoodSetImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * An "all-in-one" class for registering a set of wood-related blocks (e.g. Cherry blocks) that generate in the overworld.
 * <p>Most vanilla registration is automatically handled by this class.
 *
 * @author ebo2022
 * @since 1.0
 */
public final class WoodSet extends BlockSet<WoodSet> {

    private final WoodType woodType;
    private final Supplier<BlockBehaviour.Properties> baseProperties;
    private final Supplier<CustomBoatType> boatType;
    private MapColor barkColor = MapColor.WOOD;
    private MapColor woodColor = MapColor.PODZOL;
    private Supplier<? extends AbstractTreeGrower> treeGrower;
    private final TagKey<Block> blockLogTag;
    private final TagKey<Item> itemLogTag;
    private BlockFamily family;
    private boolean colorLeaves;
    private boolean isFull;
    public static final List<BlockVariant<WoodSet>> DEFAULT_VARIANTS = Util.make(() -> {
        ImmutableList.Builder<BlockVariant<WoodSet>> builder = ImmutableList.builder();
        builder.add(WoodVariants.STRIPPED_WOOD)
                .add(WoodVariants.STRIPPED_LOG)
                .add(WoodVariants.PLANKS)
                .add(WoodVariants.LOG)
                .add(WoodVariants.WOOD)
                .add(WoodVariants.SLAB)
                .add(WoodVariants.STAIRS)
                .add(WoodVariants.FENCE)
                .add(WoodVariants.FENCE_GATE)
                .add(WoodVariants.PRESSURE_PLATE)
                .add(WoodVariants.BUTTON)
                .add(WoodVariants.TRAPDOOR)
                .add(WoodVariants.DOOR)
                .add(WoodVariants.STANDING_SIGN)
                .add(WoodVariants.WALL_SIGN)
                .add(WoodVariants.HANGING_SIGN)
                .add(WoodVariants.WALL_HANGING_SIGN)
                .add(CommonCompatBlockVariants.WOODEN_CHEST)
                .add(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST)
                .add(CommonCompatBlockVariants.BOOKSHELF);
        WoodSetImpl.addPlatformBlockVariants(builder);
        return builder.build();
    });
    public static final List<BlockVariant<WoodSet>> EXTRA_VARIANTS = Util.make(() -> {
        ImmutableList.Builder<BlockVariant<WoodSet>> builder = ImmutableList.builder();
        builder.add(WoodVariants.LEAVES)
                .add(WoodVariants.SAPLING)
                .add(WoodVariants.POTTED_SAPLING);
        WoodSetImpl.addExtraPlatformBlockVariants(builder);
        return builder.build();
    });

    public static final List<ItemVariant<WoodSet>> DEFAULT_ITEM_VARIANTS = Util.make(() -> {
        ImmutableList.Builder<ItemVariant<WoodSet>> builder = ImmutableList.builder();
        builder.add(WoodVariants.SIGN_ITEM)
                .add(WoodVariants.HANGING_SIGN_ITEM)
                .add(WoodVariants.BOAT)
                .add(WoodVariants.CHEST_BOAT);
        WoodSetImpl.addPlatformItemVariants(builder);
        return builder.build();
    });

    @ApiStatus.Internal
    public static final DeferredRegister<CustomBoatType> BOAT_TYPE_WRITER = DeferredRegister.customWriter(BorealibRegistries.BOAT_TYPES, Borealib.MOD_ID);

    private WoodSet(String namespace, String baseName, WoodTypeProvider woodTypeProvider) {
        super(namespace, baseName);
        this.woodType = woodTypeProvider.apply(namespace, baseName);
        this.boatType = BOAT_TYPE_WRITER.register(new ResourceLocation(namespace, baseName), () -> new CustomBoatType(new ResourceLocation(namespace, "textures/entity/boat/" + baseName + ".png"), new ResourceLocation(namespace, "textures/entity/chest_boat/" + baseName + ".png")));
        this.baseProperties = () -> BlockBehaviour.Properties.of().strength(2F, 3F).sound(this.woodType.soundType()).ignitedByLava().instrument(NoteBlockInstrument.BASS);
        this.blockLogTag = TagKey.create(Registries.BLOCK, new ResourceLocation(namespace, baseName + "_logs"));
        this.itemLogTag = TagKey.create(Registries.ITEM, new ResourceLocation(namespace, baseName + "_logs"));
        DEFAULT_VARIANTS.forEach(this::include);
        DEFAULT_ITEM_VARIANTS.forEach(this::includeItem);
        ChestVariant.register(new ResourceLocation(this.getNamespace(), this.getBaseName()), false);
        ChestVariant.register(new ResourceLocation(this.getNamespace(), this.getBaseName() + "_trapped"), true);
    }

    /**
     * Creates a wood set based on the specified parameters.
     *
     * @param namespace        The namespace of all blocks to be added to this wood set
     * @param baseName         The "root" name to use for all block ids (think "oak" in "oak_log")
     * @param woodTypeProvider A provider to create a {@link WoodType} wrapping this set. It also controls the sound of the wood blocks
     * @return An unregistered {@link WoodSet} based on the provided properties
     */
    public static WoodSet of(String namespace, String baseName, WoodTypeProvider woodTypeProvider) {
        return new WoodSet(namespace, baseName, woodTypeProvider);
    }

    /**
     * Creates a wood set based on the specified parameters with a <b>default</b> {@link WoodTypeProvider}, meaning the woodset will use the new 1.20 block sounds.
     *
     * @param namespace        The namespace of all blocks to be added to this wood set
     * @param baseName         The "root" name to use for all block ids (think "oak" in "oak_log")
     * @return An unregistered {@link WoodSet} based on the provided properties
     */
    public static WoodSet of(String namespace, String baseName) {
        return of(namespace, baseName, WoodTypeProvider.DEFAULT);
    }

    /**
     * Sets the map appearance of the blocks in the wood set.
     *
     * @param barkColor The color of the outside of the log
     * @param woodColor The base wood color (whatever {@link MapColor} most closely matches the plank color)
     */
    public WoodSet color(MapColor barkColor, MapColor woodColor) {
        this.barkColor = barkColor;
        this.woodColor = woodColor;
        return this;
    }

    /**
     * Adds extra natural blocks to the wood set such as leaves and saplings
     *
     * @param treeGrower  The tree grower to use for the sapling
     * @param colorLeaves Whether the leaves have a fixed color or are tinted
     */
    public WoodSet fullSet(Supplier<? extends AbstractTreeGrower> treeGrower, boolean colorLeaves) {
        this.treeGrower = treeGrower;
        this.colorLeaves = colorLeaves;
        this.isFull = true;
        EXTRA_VARIANTS.forEach(this::include);
        return this;
    }

    /**
     * @return Whether the wood set contains extra blocks
     */
    public boolean isFull() {
        return this.isFull;
    }

    /**
     * Registers the blocks in the specified sets to the creative menu. This method can be used as a listener for {@link CreativeTabEvents#MODIFY_ENTRIES_ALL}.
     */
    public static void registerCreativeTabs(ResourceKey<CreativeModeTab> tabKey, CreativeTabEvents.Output output, WoodSet... woodSets) {
        for (WoodSet woodSet : woodSets) {
            if (tabKey == CreativeModeTabs.BUILDING_BLOCKS) {
                output.acceptAllItemsAfter(Items.MANGROVE_BUTTON, List.of(
                        woodSet.getBlock(WoodVariants.LOG),
                        woodSet.getBlock(WoodVariants.WOOD),
                        woodSet.getBlock(WoodVariants.STRIPPED_LOG),
                        woodSet.getBlock(WoodVariants.STRIPPED_WOOD),
                        woodSet.getBlock(WoodVariants.PLANKS),
                        woodSet.getBlock(WoodVariants.STAIRS),
                        woodSet.getBlock(WoodVariants.SLAB),
                        woodSet.getBlock(WoodVariants.FENCE),
                        woodSet.getBlock(WoodVariants.FENCE_GATE),
                        woodSet.getBlock(WoodVariants.DOOR),
                        woodSet.getBlock(WoodVariants.TRAPDOOR),
                        woodSet.getBlock(WoodVariants.PRESSURE_PLATE),
                        woodSet.getBlock(WoodVariants.BUTTON)
                ));
            } else if (tabKey == CreativeModeTabs.NATURAL_BLOCKS) {
                if (woodSet.isFull()) {
                    output.acceptAfter(Items.FLOWERING_AZALEA_LEAVES, woodSet.getBlock(WoodVariants.LEAVES));
                    output.acceptAfter(Items.DARK_OAK_SAPLING, woodSet.getBlock(WoodVariants.SAPLING));
                }
                output.acceptAfter(Items.MANGROVE_LOG, woodSet.getBlock(WoodVariants.LOG));
            } else if (tabKey == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
                output.acceptAfter(Items.MANGROVE_SIGN, woodSet.getItem(WoodVariants.SIGN_ITEM));
            } else if (tabKey == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                output.acceptAllItemsAfter(Items.MANGROVE_CHEST_BOAT, List.of(
                        woodSet.getItem(WoodVariants.BOAT),
                        woodSet.getItem(WoodVariants.CHEST_BOAT)
                ));
            }
        }
    }

    /**
     * Used during data generation. Do not call under normal circumstances as the blocks might not be registered.
     *
     * @return A block family wrapping the woodset
     */
    public BlockFamily getOrCreateBlockFamily() {
        if (this.family == null) {
            this.family = new BlockFamily.Builder(this.variantOrThrow(WoodVariants.PLANKS).get())
                    .button(this.getBlock(WoodVariants.BUTTON))
                    .fence(this.getBlock(WoodVariants.FENCE))
                    .fenceGate(this.getBlock(WoodVariants.FENCE_GATE))
                    .pressurePlate(this.getBlock(WoodVariants.PRESSURE_PLATE))
                    .sign(this.getBlock(WoodVariants.STANDING_SIGN), this.getBlock(WoodVariants.WALL_SIGN))
                    .slab(this.getBlock(WoodVariants.SLAB))
                    .stairs(this.getBlock(WoodVariants.STAIRS))
                    .door(this.getBlock(WoodVariants.DOOR))
                    .trapdoor(this.getBlock(WoodVariants.TRAPDOOR))
                    .recipeGroupPrefix("wooden")
                    .recipeUnlockedBy("has_planks")
                    .getFamily();
        }
        return this.family;
    }

    /**
     * @return A supplier for unconfigured properties in this set
     */
    public Supplier<BlockBehaviour.Properties> getBaseProperties() {
        return this.baseProperties;
    }

    /**
     * @return The tree grower for the sapling if it exists
     */
    public Supplier<? extends AbstractTreeGrower> getTreeGrower() {
        return this.treeGrower;
    }

    /**
     * @return The boat type for this wood set
     */
    public CustomBoatType getBoatType() {
        return this.boatType.get();
    }

    /**
     * @return A vanilla {@link WoodType} wrapping this wood set
     */
    public WoodType getWoodType() {
        return this.woodType;
    }

    /**
     * @return The outside log color to display on maps
     */
    public MapColor getBarkColor() {
        return this.barkColor;
    }

    /**
     * @return The generic wood color (i.e planks)
     */
    public MapColor getWoodColor() {
        return this.woodColor;
    }

    /**
     * @return A tag to fill with all log blocks in this wood set
     */
    public TagKey<Block> getBlockLogTag() {
        return this.blockLogTag;
    }

    /**
     * @return A tag to fill with all log items in this wood set
     */
    public TagKey<Item> getItemLogTag() {
        return this.itemLogTag;
    }

    /**
     * @return Whether the leaves have a changing foliage color (if they exist)
     */
    public boolean colorLeaves() {
        return this.colorLeaves;
    }

    @Override
    protected WoodSet getThis() {
        return this;
    }

    /**
     * Provides sounds for blocks that belong to a wood set.
     *
     * @param soundType             The generic block sound type
     * @param doorClose             The sound to play when a door closes
     * @param doorOpen              The sound to play when a door opens
     * @param trapdoorClose         The sound to play when a trapdoor closes
     * @param trapdoorOpen          The sound to play when a trapdoor opens
     * @param pressurePlateClickOff The sound to play when a player steps off a pressure plate
     * @param pressurePlateClickOn  The sound to play when a player steps on a pressure plate
     * @param buttonClickOff        The sound to play at the end of a button clicking
     * @param buttonClickOn         The sound to play at the start of a button clicking
     * @param hangingSignSoundType  A sound type to use for the hanging sign block
     * @param fenceGateClose        A sound event to use for the fence gate opening
     * @param fenceGateOpen         A sound event to use for the fence gate closing
     * @since 1.0
     */
    public record WoodTypeProvider(SoundType soundType,
                                   SoundEvent doorClose,
                                   SoundEvent doorOpen,
                                   SoundEvent trapdoorClose,
                                   SoundEvent trapdoorOpen,
                                   SoundEvent pressurePlateClickOff,
                                   SoundEvent pressurePlateClickOn,
                                   SoundEvent buttonClickOff,
                                   SoundEvent buttonClickOn,
                                   SoundType hangingSignSoundType,
                                   SoundEvent fenceGateClose,
                                   SoundEvent fenceGateOpen) implements BiFunction<String, String, WoodType> {
        public static final WoodTypeProvider DEFAULT = new WoodTypeProvider(SoundType.CHERRY_WOOD,
                SoundEvents.CHERRY_WOOD_DOOR_CLOSE,
                SoundEvents.CHERRY_WOOD_DOOR_OPEN,
                SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE,
                SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN,
                SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.CHERRY_WOOD_BUTTON_CLICK_OFF,
                SoundEvents.CHERRY_WOOD_BUTTON_CLICK_ON,
                SoundType.CHERRY_WOOD_HANGING_SIGN,
                SoundEvents.CHERRY_WOOD_FENCE_GATE_CLOSE,
                SoundEvents.CHERRY_WOOD_FENCE_GATE_OPEN
                );
        public static final WoodTypeProvider LEGACY = new WoodTypeProvider(SoundType.WOOD,
                SoundEvents.WOODEN_DOOR_CLOSE,
                SoundEvents.WOODEN_DOOR_OPEN,
                SoundEvents.WOODEN_TRAPDOOR_CLOSE,
                SoundEvents.WOODEN_TRAPDOOR_OPEN,
                SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.WOODEN_BUTTON_CLICK_OFF,
                SoundEvents.WOODEN_BUTTON_CLICK_ON,
                SoundType.HANGING_SIGN,
                SoundEvents.FENCE_GATE_CLOSE,
                SoundEvents.FENCE_GATE_OPEN);
        public static final WoodTypeProvider MUSHROOM_LIKE = new WoodTypeProvider(SoundType.NETHER_WOOD,
                SoundEvents.NETHER_WOOD_DOOR_CLOSE,
                SoundEvents.NETHER_WOOD_DOOR_OPEN,
                SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE,
                SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN,
                SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF,
                SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON,
                SoundType.NETHER_WOOD_HANGING_SIGN,
                SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE,
                SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN);
        public static final WoodTypeProvider BAMBOO_LIKE = new WoodTypeProvider(SoundType.BAMBOO_WOOD,
                SoundEvents.BAMBOO_WOOD_DOOR_CLOSE,
                SoundEvents.BAMBOO_WOOD_DOOR_OPEN,
                SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE,
                SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN,
                SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF,
                SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON,
                SoundType.BAMBOO_WOOD_HANGING_SIGN,
                SoundEvents.BAMBOO_WOOD_FENCE_GATE_CLOSE,
                SoundEvents.BAMBOO_WOOD_FENCE_GATE_OPEN);

        @Override
        public WoodType apply(String namespace, String baseName) {
            BlockSetType blockSetType = new BlockSetType(namespace + ":" + baseName, true, soundType, doorClose, doorOpen, trapdoorClose, trapdoorOpen, pressurePlateClickOff, pressurePlateClickOn, buttonClickOff, buttonClickOn);
            BlockSetType.register(blockSetType);
            WoodType woodType1 = new WoodType(blockSetType.name(), blockSetType, soundType, hangingSignSoundType, fenceGateClose, fenceGateOpen);
            WoodType.register(woodType1);
            return woodType1;
        }
    }
}

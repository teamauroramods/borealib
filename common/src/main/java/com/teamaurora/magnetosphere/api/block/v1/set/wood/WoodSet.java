package com.teamaurora.magnetosphere.api.block.v1.set.wood;

import com.teamaurora.magnetosphere.api.block.v1.set.BlockSet;
import com.teamaurora.magnetosphere.api.event.creativetabs.v1.CreativeTabEvents;
import com.teamaurora.magnetosphere.api.item.v1.CustomBoatType;
import com.teamaurora.magnetosphere.core.registry.MagnetosphereRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class WoodSet extends BlockSet<WoodSet> {

    private final WoodType woodType;
    private final Supplier<BlockBehaviour.Properties> baseProperties;
    private final CustomBoatType boatType;
    private MaterialColor barkColor = MaterialColor.WOOD;
    private MaterialColor woodColor = MaterialColor.PODZOL;
    private Supplier<AbstractTreeGrower> treeGrower;

    private WoodSet(String namespace, String baseName, WoodTypeProvider woodTypeProvider) {
        super(namespace, baseName);
        this.woodType = woodTypeProvider.apply(namespace, baseName);
        this.boatType = Registry.register(MagnetosphereRegistries.BOAT_TYPES,
                new ResourceLocation(namespace, baseName),
                new CustomBoatType(new ResourceLocation(namespace, "textures/entity/boat/" + baseName + ".png"), new ResourceLocation(namespace, "textures/entity/chest_boat/" + baseName + ".png"))
        );
        this.baseProperties = () -> BlockBehaviour.Properties.of(Material.WOOD).strength(2F, 3F).sound(this.woodType.soundType());
        this.include(WoodVariants.STRIPPED_WOOD,
                        WoodVariants.STRIPPED_LOG,
                        WoodVariants.PLANKS,
                        WoodVariants.LOG,
                        WoodVariants.WOOD,
                        WoodVariants.SLAB,
                        WoodVariants.STAIRS,
                        WoodVariants.FENCE,
                        WoodVariants.FENCE_GATE,
                        WoodVariants.PRESSURE_PLATE,
                        WoodVariants.BUTTON,
                        WoodVariants.TRAPDOOR,
                        WoodVariants.DOOR,
                        WoodVariants.STANDING_SIGN,
                        WoodVariants.WALL_SIGN)
                .includeItem(WoodVariants.SIGN_ITEM,
                        WoodVariants.BOAT,
                        WoodVariants.CHEST_BOAT);
    }

    public static WoodSet of(String namespace, String baseName, WoodTypeProvider woodTypeProvider) {
        return new WoodSet(namespace, baseName, woodTypeProvider);
    }

    public static WoodSet of(String namespace, String baseName) {
        return of(namespace, baseName, WoodTypeProvider.DEFAULT);
    }

    public WoodSet color(MaterialColor barkColor, MaterialColor woodColor) {
        this.barkColor = barkColor;
        this.woodColor = woodColor;
        return this;
    }

    public WoodSet includeLeaves() {
        return this.include(WoodVariants.LEAVES);
    }

    public WoodSet includeSapling(Supplier<AbstractTreeGrower> treeGrower) {
        this.treeGrower = treeGrower;
        return this.include(WoodVariants.SAPLING, WoodVariants.POTTED_SAPLING);
    }

    public void registerCreativeTabs() {
        CreativeTabEvents.MODIFY_ENTRIES_ALL.register((tab, flags, parameters, output, canUseGameMasterBlocks) -> {
            if (tab == CreativeModeTabs.BUILDING_BLOCKS) {
                output.acceptAllItemsAfter(Items.MANGROVE_BUTTON, List.of(
                        this.variantOrThrow(WoodVariants.LOG).get(),
                        this.variantOrThrow(WoodVariants.WOOD).get(),
                        this.variantOrThrow(WoodVariants.STRIPPED_LOG).get(),
                        this.variantOrThrow(WoodVariants.STRIPPED_WOOD).get(),
                        this.variantOrThrow(WoodVariants.PLANKS).get(),
                        this.variantOrThrow(WoodVariants.STAIRS).get(),
                        this.variantOrThrow(WoodVariants.SLAB).get(),
                        this.variantOrThrow(WoodVariants.FENCE).get(),
                        this.variantOrThrow(WoodVariants.FENCE_GATE).get(),
                        this.variantOrThrow(WoodVariants.DOOR).get(),
                        this.variantOrThrow(WoodVariants.TRAPDOOR).get(),
                        this.variantOrThrow(WoodVariants.PRESSURE_PLATE).get(),
                        this.variantOrThrow(WoodVariants.BUTTON).get()
                ));
            } else if (tab == CreativeModeTabs.NATURAL_BLOCKS) {
                output.acceptAfter(Items.MANGROVE_LOG, this.variantOrThrow(WoodVariants.LOG).get());
                this.variant(WoodVariants.LEAVES).ifPresent(r -> output.acceptAfter(Items.FLOWERING_AZALEA_LEAVES, r.get()));
                this.variant(WoodVariants.SAPLING).ifPresent(r -> output.acceptAfter(Items.DARK_OAK_SAPLING, r.get()));
            } else if (tab == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
                output.acceptAfter(Items.MANGROVE_SIGN, this.itemVariantOrThrow(WoodVariants.SIGN_ITEM).get());
            } else if (tab == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                output.acceptAllItemsAfter(Items.MANGROVE_CHEST_BOAT, List.of(
                        this.itemVariantOrThrow(WoodVariants.BOAT).get(),
                        this.itemVariantOrThrow(WoodVariants.CHEST_BOAT).get()
                ));
            }
        });
    }

    public Supplier<BlockBehaviour.Properties> getBaseProperties() {
        return this.baseProperties;
    }

    public Supplier<AbstractTreeGrower> getTreeGrower() {
        return this.treeGrower;
    }

    public CustomBoatType getBoatType() {
        return this.boatType;
    }

    public WoodType getWoodType() {
        return this.woodType;
    }

    public MaterialColor getBarkColor() {
        return this.barkColor;
    }

    public MaterialColor getWoodColor() {
        return this.woodColor;
    }

    @Override
    protected WoodSet getThis() {
        return this;
    }

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
            BlockSetType blockSetType = new BlockSetType(namespace + ":" + baseName, soundType, doorClose, doorOpen, trapdoorClose, trapdoorOpen, pressurePlateClickOff, pressurePlateClickOn, buttonClickOff, buttonClickOn);
            BlockSetType.register(blockSetType);
            WoodType woodType1 = new WoodType(blockSetType.name(), blockSetType, soundType, hangingSignSoundType, fenceGateClose, fenceGateOpen);
            WoodType.register(woodType1);
            return woodType1;
        }
    }
}

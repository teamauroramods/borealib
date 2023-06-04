package com.teamaurora.magnetosphere.api.block.v1.set.wood;

import com.teamaurora.magnetosphere.api.block.v1.BlockUtils;
import com.teamaurora.magnetosphere.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.magnetosphere.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.magnetosphere.api.block.v1.set.variant.ItemVariant;
import com.teamaurora.magnetosphere.api.content_registries.v1.client.render.RenderTypeRegistry;
import com.teamaurora.magnetosphere.api.item.v1.CustomBoatItem;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import com.teamaurora.magnetosphere.core.extensions.BlockEntityTypeExtension;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.apache.logging.log4j.util.TriConsumer;

public final class WoodVariants {

    public static final BlockVariant<WoodSet> STRIPPED_WOOD = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(plankColors(set).explosionResistance(2.0f)))
            .prefix("stripped")
            .suffix("wood")
            .build();
    public static final BlockVariant<WoodSet> STRIPPED_LOG = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(plankColors(set).explosionResistance(2.0f)))
            .prefix("stripped")
            .suffix("log")
            .build();
    public static final BlockVariant<WoodSet> PLANKS = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(plankColors(set)))
            .suffix("planks")
            .build();
    public static final BlockVariant<WoodSet> LOG = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(axisDependentColors(set).explosionResistance(2.0f)))
            .suffix("log")
            .build();
    public static final BlockVariant<WoodSet> WOOD = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(barkColors(set).explosionResistance(2.0f)))
            .suffix("wood")
            .build();
    public static final BlockVariant<WoodSet> SLAB = BlockVariant.<WoodSet>builder(set ->
                    () -> new SlabBlock(plankColors(set)))
            .suffix("slab")
            .build();
    public static final BlockVariant<WoodSet> STAIRS = BlockVariant.<WoodSet>builder(set ->
                    () -> new StairBlock(set.variantOrThrow(PLANKS).get().defaultBlockState(), plankColors(set)))
            .suffix("stairs")
            .build();
    public static final BlockVariant<WoodSet> FENCE = BlockVariant.<WoodSet>builder(set ->
                    () -> new FenceBlock(plankColors(set)))
            .suffix("fence")
            .build();
    public static final BlockVariant<WoodSet> FENCE_GATE = BlockVariant.<WoodSet>builder(set ->
                    () -> new FenceGateBlock(plankColors(set), set.getWoodType()))
            .suffix("fence_gate")
            .build();
    public static final BlockVariant<WoodSet> PRESSURE_PLATE = BlockVariant.<WoodSet>builder(set ->
                    () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, plankColors(set).strength(0.5f).noCollission(), set.getWoodType().setType()))
            .suffix("pressure_plate")
            .build();
    public static final BlockVariant<WoodSet> BUTTON = BlockVariant.<WoodSet>builder(set ->
                    () -> new ButtonBlock(plankColors(set).strength(0.5F).noCollission(), set.getWoodType().setType(), 30, true))
            .suffix("button")
            .build();
    public static final BlockVariant<WoodSet> TRAPDOOR = BlockVariant.<WoodSet>builder(set ->
                    () -> new TrapDoorBlock(plankColors(set).strength(3F).noOcclusion(), set.getWoodType().setType()))
            .suffix("trapdoor")
            .clientPostInit(() -> (d, s, o) -> {
                d.enqueueWork(() -> RenderTypeRegistry.register(RenderType.cutout(), o.get()));
            })
            .build();
    public static final BlockVariant<WoodSet> DOOR = BlockVariant.<WoodSet>builder(set ->
                    () -> new DoorBlock(plankColors(set).strength(3F).noOcclusion(), set.getWoodType().setType()))
            .suffix("door")
            .clientPostInit(() -> (d, s, o) -> {
                d.enqueueWork(() -> RenderTypeRegistry.register(RenderType.cutout(), o.get()));
            })
            .build();
    public static final BlockVariant<WoodSet> STANDING_SIGN = BlockVariant.<WoodSet>builder(set ->
            () -> new StandingSignBlock(plankColors(set).strength(1F).noCollission(), set.getWoodType()))
            .noBlockItem()
            .suffix("sign")
            .onRegister(block -> ((BlockEntityTypeExtension) BlockEntityType.SIGN).magnetosphere$addAdditionalBlockTypes(block))
            .build();
    public static final BlockVariant<WoodSet> WALL_SIGN = BlockVariant.<WoodSet>builder(set ->
                    () -> new WallSignBlock(plankColors(set).strength(1F).noCollission(), set.getWoodType()))
            .noBlockItem()
            .suffix("wall_sign")
            .onRegister(block -> ((BlockEntityTypeExtension) BlockEntityType.SIGN).magnetosphere$addAdditionalBlockTypes(block))
            .build();
    public static final ItemVariant<WoodSet> SIGN_ITEM = ItemVariant.<WoodSet>builder(set ->
                    () -> new SignItem(new Item.Properties().stacksTo(16), set.variantOrThrow(STANDING_SIGN).get(), set.variantOrThrow(WALL_SIGN).get()))
            .suffix("sign")
            .build();
    public static final BlockVariant<WoodSet> LEAVES = BlockVariant.<WoodSet>builder(set ->
                    () -> new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.AZALEA_LEAVES).noOcclusion().isValidSpawn(WoodVariants::ocelotOrParrot).isSuffocating(WoodVariants::never).isViewBlocking(WoodVariants::never)))
            .suffix("leaves")
            .build();
    public static final BlockVariant<WoodSet> SAPLING = BlockVariant.<WoodSet>builder(set ->
                    () -> new SaplingBlock(set.getTreeGrower().get(), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CHERRY_SAPLING)))
            .suffix("sapling")
            .clientPostInit(() -> (d, s, o) -> {
                d.enqueueWork(() -> RenderTypeRegistry.register(RenderType.cutout(), o.get()));
            })
            .build();
    public static final BlockVariant<WoodSet> POTTED_SAPLING = BlockVariant.<WoodSet>builder(set ->
                    () -> new FlowerPotBlock(set.variantOrThrow(SAPLING).get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()))
            .noBlockItem()
            .prefix("potted")
            .suffix("sapling")
            .clientPostInit(() -> (d, s, o) -> {
                d.enqueueWork(() -> RenderTypeRegistry.register(RenderType.cutout(), o.get()));
            })
            .build();
    public static final ItemVariant<WoodSet> BOAT = ItemVariant.<WoodSet>builder(set ->
                    () -> new CustomBoatItem(set.getBoatType(), false, new Item.Properties().stacksTo(1)))
            .suffix("boat")
            .build();
    public static final ItemVariant<WoodSet> CHEST_BOAT = ItemVariant.<WoodSet>builder(set ->
                    () -> new CustomBoatItem(set.getBoatType(), true, new Item.Properties().stacksTo(1)))
            .suffix("chest_boat")
            .build();

    private WoodVariants() {
    }

    private static BlockBehaviour.Properties axisDependentColors(WoodSet set) {
        return BlockUtils.colorFunction(set.getBaseProperties().get(), blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? set.getWoodColor() : set.getBarkColor());
    }

    private static BlockBehaviour.Properties plankColors(WoodSet set) {
        return set.getBaseProperties().get().color(set.getWoodColor());
    }

    private static BlockBehaviour.Properties barkColors(WoodSet set) {
        return set.getBaseProperties().get().color(set.getWoodColor());
    }

    private static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    private static Boolean ocelotOrParrot(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
    }
}

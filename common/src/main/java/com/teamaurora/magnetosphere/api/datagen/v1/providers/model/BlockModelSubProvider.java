package com.teamaurora.magnetosphere.api.datagen.v1.providers.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BlockModelSubProvider implements ModelSubProvider {

    private final Consumer<BlockStateGenerator> blockStateOutput;
    private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
    private final Consumer<Item> skippedAutoModelsOutput;
    static final Map<BlockFamily.Variant, BiConsumer<BlockFamilyProvider, Block>> SHAPE_CONSUMERS = ImmutableMap.<BlockFamily.Variant, BiConsumer<BlockFamilyProvider, Block>>builder()
            .put(BlockFamily.Variant.BUTTON, BlockFamilyProvider::button)
            .put(BlockFamily.Variant.DOOR, BlockFamilyProvider::door)
            .put(BlockFamily.Variant.CHISELED, BlockFamilyProvider::fullBlockVariant)
            .put(BlockFamily.Variant.CRACKED, BlockFamilyProvider::fullBlockVariant)
            .put(BlockFamily.Variant.CUSTOM_FENCE, BlockFamilyProvider::customFence)
            .put(BlockFamily.Variant.FENCE, BlockFamilyProvider::fence)
            .put(BlockFamily.Variant.CUSTOM_FENCE_GATE, BlockFamilyProvider::customFenceGate)
            .put(BlockFamily.Variant.FENCE_GATE, BlockFamilyProvider::fenceGate)
            .put(BlockFamily.Variant.SIGN, BlockFamilyProvider::sign)
            .put(BlockFamily.Variant.SLAB, BlockFamilyProvider::slab)
            .put(BlockFamily.Variant.STAIRS, BlockFamilyProvider::stairs)
            .put(BlockFamily.Variant.PRESSURE_PLATE, BlockFamilyProvider::pressurePlate)
            .put(BlockFamily.Variant.TRAPDOOR, BlockFamilyProvider::trapdoor)
            .put(BlockFamily.Variant.WALL, BlockFamilyProvider::wall)
            .build();

    public BlockModelSubProvider(Consumer<BlockStateGenerator> blockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput, Consumer<Item> skippedAutoModelsOutput) {
        this.blockStateOutput = blockStateOutput;
        this.modelOutput = modelOutput;
        this.skippedAutoModelsOutput = skippedAutoModelsOutput;
    }

    public Map<Block, TexturedModel> getTexturedModels() {
        return Collections.emptyMap();
    }

    public List<Block> getNonOrientableTrapdoors() {
        return Collections.emptyList();
    }

    public Map<Block, BlockStateGeneratorSupplier> getFullBlockModelCustomGenerators() {
        return Collections.emptyMap();
    }


    public Consumer<BlockStateGenerator> blockStateOutput() {
        return blockStateOutput;
    }

    public BiConsumer<ResourceLocation, Supplier<JsonElement>> getModelOutput() {
        return modelOutput;
    }

    public Consumer<Item> getSkippedAutoModelsOutput() {
        return skippedAutoModelsOutput;
    }

    protected void skipAutoItemBlock(Block block) {
        this.skippedAutoModelsOutput.accept(block.asItem());
    }

    protected void delegateItemModel(Block block, ResourceLocation delegateModelLocation) {
        this.modelOutput.accept(ModelLocationUtils.getModelLocation(block.asItem()), new DelegatedModel(delegateModelLocation));
    }

    protected void delegateItemModel(Item item, ResourceLocation delegateModelLocation) {
        this.modelOutput.accept(ModelLocationUtils.getModelLocation(item), new DelegatedModel(delegateModelLocation));
    }

    protected void createSimpleFlatItemModel(Item flatItem) {
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(flatItem), TextureMapping.layer0(flatItem), this.modelOutput);
    }

    protected void createSimpleFlatItemModel(Block flatBlock) {
        Item item = flatBlock.asItem();
        if (item != Items.AIR)
            ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(flatBlock), this.modelOutput);
    }

    protected void createSimpleFlatItemModel(Block flatBlock, String layerZeroTextureSuffix) {
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(flatBlock.asItem()), TextureMapping.layer0(TextureMapping.getBlockTexture(flatBlock, layerZeroTextureSuffix)), this.modelOutput);
    }

    protected static PropertyDispatch createHorizontalFacingDispatch() {
        return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING).select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.NORTH, Variant.variant());
    }

    protected static PropertyDispatch createHorizontalFacingDispatchAlt() {
        return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING).select(Direction.SOUTH, Variant.variant()).select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
    }

    protected static PropertyDispatch createTorchHorizontalDispatch() {
        return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING).select(Direction.EAST, Variant.variant()).select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
    }

    protected static PropertyDispatch createFacingDispatch() {
        return PropertyDispatch.property(BlockStateProperties.FACING).select(Direction.DOWN, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select(Direction.UP, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)).select(Direction.NORTH, Variant.variant()).select(Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }

    protected static MultiVariantGenerator createRotatedVariant(Block block, ResourceLocation modelLocation) {
        return MultiVariantGenerator.multiVariant(block, createRotatedVariants(modelLocation));
    }

    protected static Variant[] createRotatedVariants(ResourceLocation modelLocation) {
        return new Variant[]{Variant.variant().with(VariantProperties.MODEL, modelLocation), Variant.variant().with(VariantProperties.MODEL, modelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90), Variant.variant().with(VariantProperties.MODEL, modelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), Variant.variant().with(VariantProperties.MODEL, modelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)};
    }

    protected static MultiVariantGenerator createRotatedVariant(Block block, ResourceLocation normalModelLocation, ResourceLocation mirroredModelLocation) {
        return MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, normalModelLocation), Variant.variant().with(VariantProperties.MODEL, mirroredModelLocation), Variant.variant().with(VariantProperties.MODEL, normalModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), Variant.variant().with(VariantProperties.MODEL, mirroredModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180));
    }

    protected static PropertyDispatch createBooleanModelDispatch(BooleanProperty property, ResourceLocation trueModelLocation, ResourceLocation falseModelLocation) {
        return PropertyDispatch.property(property).select(true, Variant.variant().with(VariantProperties.MODEL, trueModelLocation)).select(false, Variant.variant().with(VariantProperties.MODEL, falseModelLocation));
    }

    protected void createRotatedMirroredVariantBlock(Block block) {
        ResourceLocation resourceLocation = TexturedModel.CUBE.create(block, this.modelOutput);
        ResourceLocation resourceLocation2 = TexturedModel.CUBE_MIRRORED.create(block, this.modelOutput);
        this.blockStateOutput.accept(createRotatedVariant(block, resourceLocation, resourceLocation2));
    }

    protected void createRotatedVariantBlock(Block block) {
        ResourceLocation resourceLocation = TexturedModel.CUBE.create(block, this.modelOutput);
        this.blockStateOutput.accept(createRotatedVariant(block, resourceLocation));
    }

    protected static BlockStateGenerator createCustomFence(Block block, ResourceLocation resourceLocation, ResourceLocation resourceLocation2, ResourceLocation resourceLocation3, ResourceLocation resourceLocation4, ResourceLocation resourceLocation5) {
        return MultiPartGenerator.multiPart(block).with(Variant.variant().with(VariantProperties.MODEL, resourceLocation)).with(Condition.condition().term(BlockStateProperties.NORTH, true), Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.UV_LOCK, false)).with(Condition.condition().term(BlockStateProperties.EAST, true), Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.UV_LOCK, false)).with(Condition.condition().term(BlockStateProperties.SOUTH, true), Variant.variant().with(VariantProperties.MODEL, resourceLocation4).with(VariantProperties.UV_LOCK, false)).with(Condition.condition().term(BlockStateProperties.WEST, true), Variant.variant().with(VariantProperties.MODEL, resourceLocation5).with(VariantProperties.UV_LOCK, false));
    }

    protected static BlockStateGenerator createButton(Block buttonBlock, ResourceLocation unpoweredModelLocation, ResourceLocation poweredModelLocation) {
        return MultiVariantGenerator.multiVariant(buttonBlock).with(PropertyDispatch.property(BlockStateProperties.POWERED).select(false, Variant.variant().with(VariantProperties.MODEL, unpoweredModelLocation)).select(true, Variant.variant().with(VariantProperties.MODEL, poweredModelLocation))).with(PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING).select(AttachFace.FLOOR, Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(AttachFace.FLOOR, Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(AttachFace.FLOOR, Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(AttachFace.FLOOR, Direction.NORTH, Variant.variant()).select(AttachFace.WALL, Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(AttachFace.WALL, Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(AttachFace.WALL, Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(AttachFace.WALL, Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(AttachFace.CEILING, Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)).select(AttachFace.CEILING, Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)));
    }

    protected static PropertyDispatch.C4<Direction, DoubleBlockHalf, DoorHingeSide, Boolean> configureDoorHalf(PropertyDispatch.C4<Direction, DoubleBlockHalf, DoorHingeSide, Boolean> c4, DoubleBlockHalf doubleBlockHalf, ResourceLocation resourceLocation, ResourceLocation resourceLocation2, ResourceLocation resourceLocation3, ResourceLocation resourceLocation4) {
        return c4.select(Direction.EAST, doubleBlockHalf, DoorHingeSide.LEFT, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation)).select(Direction.SOUTH, doubleBlockHalf, DoorHingeSide.LEFT, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.WEST, doubleBlockHalf, DoorHingeSide.LEFT, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.NORTH, doubleBlockHalf, DoorHingeSide.LEFT, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.EAST, doubleBlockHalf, DoorHingeSide.RIGHT, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation3)).select(Direction.SOUTH, doubleBlockHalf, DoorHingeSide.RIGHT, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.WEST, doubleBlockHalf, DoorHingeSide.RIGHT, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.NORTH, doubleBlockHalf, DoorHingeSide.RIGHT, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.EAST, doubleBlockHalf, DoorHingeSide.LEFT, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.SOUTH, doubleBlockHalf, DoorHingeSide.LEFT, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.WEST, doubleBlockHalf, DoorHingeSide.LEFT, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.NORTH, doubleBlockHalf, DoorHingeSide.LEFT, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation2)).select(Direction.EAST, doubleBlockHalf, DoorHingeSide.RIGHT, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.SOUTH, doubleBlockHalf, DoorHingeSide.RIGHT, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation4)).select(Direction.WEST, doubleBlockHalf, DoorHingeSide.RIGHT, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.NORTH, doubleBlockHalf, DoorHingeSide.RIGHT, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180));
    }

    protected static BlockStateGenerator createFence(Block fenceBlock, ResourceLocation fencePostModelLocation, ResourceLocation fenceSideModelLocation) {
        return MultiPartGenerator.multiPart(fenceBlock).with(Variant.variant().with(VariantProperties.MODEL, fencePostModelLocation)).with(Condition.condition().term(BlockStateProperties.NORTH, true), Variant.variant().with(VariantProperties.MODEL, fenceSideModelLocation).with(VariantProperties.UV_LOCK, true)).with(Condition.condition().term(BlockStateProperties.EAST, true), Variant.variant().with(VariantProperties.MODEL, fenceSideModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).with(Condition.condition().term(BlockStateProperties.SOUTH, true), Variant.variant().with(VariantProperties.MODEL, fenceSideModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).with(Condition.condition().term(BlockStateProperties.WEST, true), Variant.variant().with(VariantProperties.MODEL, fenceSideModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true));
    }

    protected static BlockStateGenerator createWall(Block wallBlock, ResourceLocation postModelLocation, ResourceLocation lowSideModelLocation, ResourceLocation tallSideModelLocation) {
        return MultiPartGenerator.multiPart(wallBlock).with(Condition.condition().term(BlockStateProperties.UP, true), Variant.variant().with(VariantProperties.MODEL, postModelLocation)).with(Condition.condition().term(BlockStateProperties.NORTH_WALL, WallSide.LOW), Variant.variant().with(VariantProperties.MODEL, lowSideModelLocation).with(VariantProperties.UV_LOCK, true)).with(Condition.condition().term(BlockStateProperties.EAST_WALL, WallSide.LOW), Variant.variant().with(VariantProperties.MODEL, lowSideModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).with(Condition.condition().term(BlockStateProperties.SOUTH_WALL, WallSide.LOW), Variant.variant().with(VariantProperties.MODEL, lowSideModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).with(Condition.condition().term(BlockStateProperties.WEST_WALL, WallSide.LOW), Variant.variant().with(VariantProperties.MODEL, lowSideModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)).with(Condition.condition().term(BlockStateProperties.NORTH_WALL, WallSide.TALL), Variant.variant().with(VariantProperties.MODEL, tallSideModelLocation).with(VariantProperties.UV_LOCK, true)).with(Condition.condition().term(BlockStateProperties.EAST_WALL, WallSide.TALL), Variant.variant().with(VariantProperties.MODEL, tallSideModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).with(Condition.condition().term(BlockStateProperties.SOUTH_WALL, WallSide.TALL), Variant.variant().with(VariantProperties.MODEL, tallSideModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).with(Condition.condition().term(BlockStateProperties.WEST_WALL, WallSide.TALL), Variant.variant().with(VariantProperties.MODEL, tallSideModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true));
    }

    protected static BlockStateGenerator createFenceGate(Block block, ResourceLocation resourceLocation, ResourceLocation resourceLocation2, ResourceLocation resourceLocation3, ResourceLocation resourceLocation4, boolean bl) {
        return MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.UV_LOCK, bl)).with(createHorizontalFacingDispatchAlt()).with(PropertyDispatch.properties(BlockStateProperties.IN_WALL, BlockStateProperties.OPEN).select(false, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation2)).select(true, false, Variant.variant().with(VariantProperties.MODEL, resourceLocation4)).select(false, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation)).select(true, true, Variant.variant().with(VariantProperties.MODEL, resourceLocation3)));
    }

    protected static BlockStateGenerator createStairs(Block stairsBlock, ResourceLocation innerModelLocation, ResourceLocation straightModelLocation, ResourceLocation outerModelLocation) {
        return MultiVariantGenerator.multiVariant(stairsBlock).with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.STAIRS_SHAPE).select(Direction.EAST, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightModelLocation)).select(Direction.WEST, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.SOUTH, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(Direction.NORTH, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)).select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation)).select(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)).select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)).select(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation)).select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation)).select(Direction.WEST, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)).select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)).select(Direction.WEST, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation)).select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.EAST, Half.TOP, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.WEST, Half.TOP, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.SOUTH, Half.TOP, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(Direction.NORTH, Half.TOP, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)).select(Direction.EAST, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(Direction.WEST, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)).select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.NORTH, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.EAST, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.WEST, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(Direction.NORTH, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)).select(Direction.EAST, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(Direction.WEST, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)).select(Direction.SOUTH, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.NORTH, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.EAST, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.WEST, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true)).select(Direction.SOUTH, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true)).select(Direction.NORTH, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true)));
    }

    protected static BlockStateGenerator createOrientableTrapdoor(Block orientableTrapdoorBlock, ResourceLocation topModelLocation, ResourceLocation bottomModelLocation, ResourceLocation openModelLocation) {
        return MultiVariantGenerator.multiVariant(orientableTrapdoorBlock).with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.OPEN).select(Direction.NORTH, Half.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, bottomModelLocation)).select(Direction.SOUTH, Half.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, bottomModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.EAST, Half.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, bottomModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.WEST, Half.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, bottomModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.NORTH, Half.TOP, false, Variant.variant().with(VariantProperties.MODEL, topModelLocation)).select(Direction.SOUTH, Half.TOP, false, Variant.variant().with(VariantProperties.MODEL, topModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.EAST, Half.TOP, false, Variant.variant().with(VariantProperties.MODEL, topModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.WEST, Half.TOP, false, Variant.variant().with(VariantProperties.MODEL, topModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.NORTH, Half.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation)).select(Direction.SOUTH, Half.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.EAST, Half.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.WEST, Half.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.NORTH, Half.TOP, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.SOUTH, Half.TOP, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0)).select(Direction.EAST, Half.TOP, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.WEST, Half.TOP, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));
    }

    protected static BlockStateGenerator createTrapdoor(Block trapdoorBlock, ResourceLocation topModelLocation, ResourceLocation bottomModelLocation, ResourceLocation openModelLocation) {
        return MultiVariantGenerator.multiVariant(trapdoorBlock).with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.OPEN).select(Direction.NORTH, Half.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, bottomModelLocation)).select(Direction.SOUTH, Half.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, bottomModelLocation)).select(Direction.EAST, Half.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, bottomModelLocation)).select(Direction.WEST, Half.BOTTOM, false, Variant.variant().with(VariantProperties.MODEL, bottomModelLocation)).select(Direction.NORTH, Half.TOP, false, Variant.variant().with(VariantProperties.MODEL, topModelLocation)).select(Direction.SOUTH, Half.TOP, false, Variant.variant().with(VariantProperties.MODEL, topModelLocation)).select(Direction.EAST, Half.TOP, false, Variant.variant().with(VariantProperties.MODEL, topModelLocation)).select(Direction.WEST, Half.TOP, false, Variant.variant().with(VariantProperties.MODEL, topModelLocation)).select(Direction.NORTH, Half.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation)).select(Direction.SOUTH, Half.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.EAST, Half.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.WEST, Half.BOTTOM, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)).select(Direction.NORTH, Half.TOP, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation)).select(Direction.SOUTH, Half.TOP, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)).select(Direction.EAST, Half.TOP, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)).select(Direction.WEST, Half.TOP, true, Variant.variant().with(VariantProperties.MODEL, openModelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
    }

    protected static MultiVariantGenerator createSimpleBlock(Block block, ResourceLocation modelLocation) {
        return MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, modelLocation));
    }

    protected static PropertyDispatch createRotatedPillar() {
        return PropertyDispatch.property(BlockStateProperties.AXIS).select(Direction.Axis.Y, Variant.variant()).select(Direction.Axis.Z, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select(Direction.Axis.X, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }

    protected static BlockStateGenerator createAxisAlignedPillarBlock(Block axisAlignedPillarBlock, ResourceLocation modelLocation) {
        return MultiVariantGenerator.multiVariant(axisAlignedPillarBlock, Variant.variant().with(VariantProperties.MODEL, modelLocation)).with(createRotatedPillar());
    }

    protected void createAxisAlignedPillarBlockCustomModel(Block axisAlignedPillarBlock, ResourceLocation modelLocation) {
        this.blockStateOutput.accept(createAxisAlignedPillarBlock(axisAlignedPillarBlock, modelLocation));
    }

    protected void createAxisAlignedPillarBlock(Block axisAlignedPillarBlock, TexturedModel.Provider provider) {
        ResourceLocation resourceLocation = provider.create(axisAlignedPillarBlock, this.modelOutput);
        this.blockStateOutput.accept(createAxisAlignedPillarBlock(axisAlignedPillarBlock, resourceLocation));
    }

    protected void createHorizontallyRotatedBlock(Block horizontallyRotatedBlock, TexturedModel.Provider provider) {
        ResourceLocation resourceLocation = provider.create(horizontallyRotatedBlock, this.modelOutput);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(horizontallyRotatedBlock, Variant.variant().with(VariantProperties.MODEL, resourceLocation)).with(createHorizontalFacingDispatch()));
    }

    protected static BlockStateGenerator createRotatedPillarWithHorizontalVariant(Block rotatedPillarBlock, ResourceLocation modelLocation, ResourceLocation horizontalModelLocation) {
        return MultiVariantGenerator.multiVariant(rotatedPillarBlock).with(PropertyDispatch.property(BlockStateProperties.AXIS).select(Direction.Axis.Y, Variant.variant().with(VariantProperties.MODEL, modelLocation)).select(Direction.Axis.Z, Variant.variant().with(VariantProperties.MODEL, horizontalModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)).select(Direction.Axis.X, Variant.variant().with(VariantProperties.MODEL, horizontalModelLocation).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));
    }

    protected void createRotatedPillarWithHorizontalVariant(Block rotatedPillarBlock, TexturedModel.Provider modelProvider, TexturedModel.Provider horizontalModelProvider) {
        ResourceLocation resourceLocation = modelProvider.create(rotatedPillarBlock, this.modelOutput);
        ResourceLocation resourceLocation2 = horizontalModelProvider.create(rotatedPillarBlock, this.modelOutput);
        this.blockStateOutput.accept(createRotatedPillarWithHorizontalVariant(rotatedPillarBlock, resourceLocation, resourceLocation2));
    }

    protected ResourceLocation createSuffixedVariant(Block block, String suffix, ModelTemplate modelTemplate, Function<ResourceLocation, TextureMapping> textureMappingGetter) {
        return modelTemplate.createWithSuffix(block, suffix, textureMappingGetter.apply(TextureMapping.getBlockTexture(block, suffix)), this.modelOutput);
    }

    protected static BlockStateGenerator createPressurePlate(Block pressurePlateBlock, ResourceLocation unpoweredModelLocation, ResourceLocation poweredModelLocation) {
        return MultiVariantGenerator.multiVariant(pressurePlateBlock).with(createBooleanModelDispatch(BlockStateProperties.POWERED, poweredModelLocation, unpoweredModelLocation));
    }

    protected static BlockStateGenerator createSlab(Block slabBlock, ResourceLocation bottomHalfModelLocation, ResourceLocation topHalfModelLocation, ResourceLocation doubleModelLocation) {
        return MultiVariantGenerator.multiVariant(slabBlock).with(PropertyDispatch.property(BlockStateProperties.SLAB_TYPE).select(SlabType.BOTTOM, Variant.variant().with(VariantProperties.MODEL, bottomHalfModelLocation)).select(SlabType.TOP, Variant.variant().with(VariantProperties.MODEL, topHalfModelLocation)).select(SlabType.DOUBLE, Variant.variant().with(VariantProperties.MODEL, doubleModelLocation)));
    }

    protected void createTrivialCube(Block block) {
        this.createTrivialBlock(block, TexturedModel.CUBE);
    }

    protected void createTrivialBlock(Block block, TexturedModel.Provider provider) {
        this.blockStateOutput.accept(createSimpleBlock(block, provider.create(block, this.modelOutput)));
    }

    protected void createTrivialBlock(Block block, TextureMapping textureMapping, ModelTemplate modelTemplate) {
        ResourceLocation resourceLocation = modelTemplate.create(block, textureMapping, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(block, resourceLocation));
    }

    protected BlockFamilyProvider family(Block block) {
        TexturedModel texturedModel = this.getTexturedModels().getOrDefault(block, TexturedModel.CUBE.get(block));
        return (new BlockFamilyProvider(texturedModel.getMapping())).fullBlock(block, texturedModel.getTemplate());
    }

    protected void createOrientableTrapdoor(Block orientableTrapdoorBlock) {
        TextureMapping textureMapping = TextureMapping.defaultTexture(orientableTrapdoorBlock);
        ResourceLocation resourceLocation = ModelTemplates.ORIENTABLE_TRAPDOOR_TOP.create(orientableTrapdoorBlock, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation2 = ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM.create(orientableTrapdoorBlock, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation3 = ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN.create(orientableTrapdoorBlock, textureMapping, this.modelOutput);
        this.blockStateOutput.accept(createOrientableTrapdoor(orientableTrapdoorBlock, resourceLocation, resourceLocation2, resourceLocation3));
        this.delegateItemModel(orientableTrapdoorBlock, resourceLocation2);
    }

    protected void createTrapdoor(Block trapdoorBlock) {
        TextureMapping textureMapping = TextureMapping.defaultTexture(trapdoorBlock);
        ResourceLocation resourceLocation = ModelTemplates.TRAPDOOR_TOP.create(trapdoorBlock, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation2 = ModelTemplates.TRAPDOOR_BOTTOM.create(trapdoorBlock, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation3 = ModelTemplates.TRAPDOOR_OPEN.create(trapdoorBlock, textureMapping, this.modelOutput);
        this.blockStateOutput.accept(createTrapdoor(trapdoorBlock, resourceLocation, resourceLocation2, resourceLocation3));
        this.delegateItemModel(trapdoorBlock, resourceLocation2);
    }

    protected WoodProvider woodProvider(Block logBlock) {
        return new WoodProvider(TextureMapping.logColumn(logBlock));
    }

    protected void createNonTemplateModelBlock(Block block) {
        this.createNonTemplateModelBlock(block, block);
    }

    protected void createNonTemplateModelBlock(Block block, Block modelBlock) {
        this.blockStateOutput.accept(createSimpleBlock(block, ModelLocationUtils.getModelLocation(modelBlock)));
    }

    protected void createCrossBlockWithDefaultItem(Block crossBlock, BlockModelGenerators.TintState tintState) {
        this.createSimpleFlatItemModel(crossBlock);
        this.createCrossBlock(crossBlock, tintState);
    }

    protected void createCrossBlockWithDefaultItem(Block crossBlock, BlockModelGenerators.TintState tintState, TextureMapping textureMapping) {
        this.createSimpleFlatItemModel(crossBlock);
        this.createCrossBlock(crossBlock, tintState, textureMapping);
    }

    protected void createCrossBlock(Block crossBlock, BlockModelGenerators.TintState tintState) {
        TextureMapping textureMapping = TextureMapping.cross(crossBlock);
        this.createCrossBlock(crossBlock, tintState, textureMapping);
    }

    protected void createCrossBlock(Block crossBlock, BlockModelGenerators.TintState tintState, TextureMapping textureMapping) {
        ResourceLocation resourceLocation = tintState.getCross().create(crossBlock, textureMapping, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(crossBlock, resourceLocation));
    }

    protected void createPlant(Block plantBlock, Block pottedPlantBlock, BlockModelGenerators.TintState tintState) {
        this.createCrossBlockWithDefaultItem(plantBlock, tintState);
        TextureMapping textureMapping = TextureMapping.plant(plantBlock);
        ResourceLocation resourceLocation = tintState.getCrossPot().create(pottedPlantBlock, textureMapping, this.modelOutput);
        this.blockStateOutput.accept(createSimpleBlock(pottedPlantBlock, resourceLocation));
    }

    protected void copyModel(Block sourceBlock, Block targetBlock) {
        ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(sourceBlock);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(targetBlock, Variant.variant().with(VariantProperties.MODEL, resourceLocation)));
        this.delegateItemModel(targetBlock, resourceLocation);
    }

    protected static BlockStateGenerator createDoor(Block block, ResourceLocation resourceLocation, ResourceLocation resourceLocation2, ResourceLocation resourceLocation3, ResourceLocation resourceLocation4, ResourceLocation resourceLocation5, ResourceLocation resourceLocation6, ResourceLocation resourceLocation7, ResourceLocation resourceLocation8) {
        return MultiVariantGenerator.multiVariant(block).with(configureDoorHalf(configureDoorHalf(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.DOUBLE_BLOCK_HALF, BlockStateProperties.DOOR_HINGE, BlockStateProperties.OPEN), DoubleBlockHalf.LOWER, resourceLocation, resourceLocation2, resourceLocation3, resourceLocation4), DoubleBlockHalf.UPPER, resourceLocation5, resourceLocation6, resourceLocation7, resourceLocation8));
    }

    protected void createDoor(Block block) {
        TextureMapping textureMapping = TextureMapping.door(block);
        ResourceLocation resourceLocation = ModelTemplates.DOOR_BOTTOM_LEFT.create(block, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation2 = ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(block, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation3 = ModelTemplates.DOOR_BOTTOM_RIGHT.create(block, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation4 = ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(block, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation5 = ModelTemplates.DOOR_TOP_LEFT.create(block, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation6 = ModelTemplates.DOOR_TOP_LEFT_OPEN.create(block, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation7 = ModelTemplates.DOOR_TOP_RIGHT.create(block, textureMapping, this.modelOutput);
        ResourceLocation resourceLocation8 = ModelTemplates.DOOR_TOP_RIGHT_OPEN.create(block, textureMapping, this.modelOutput);
        this.createSimpleFlatItemModel(block.asItem());
        this.blockStateOutput.accept(createDoor(block, resourceLocation, resourceLocation2, resourceLocation3, resourceLocation4, resourceLocation5, resourceLocation6, resourceLocation7, resourceLocation8));
    }

    protected static BlockStateGenerator createPillarBlockUVLocked(Block block, TextureMapping textureMapping, BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
        ResourceLocation resourceLocation = ModelTemplates.CUBE_COLUMN_UV_LOCKED_X.create(block, textureMapping, biConsumer);
        ResourceLocation resourceLocation2 = ModelTemplates.CUBE_COLUMN_UV_LOCKED_Y.create(block, textureMapping, biConsumer);
        ResourceLocation resourceLocation3 = ModelTemplates.CUBE_COLUMN_UV_LOCKED_Z.create(block, textureMapping, biConsumer);
        ResourceLocation resourceLocation4 = ModelTemplates.CUBE_COLUMN.create(block, textureMapping, biConsumer);
        return MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, resourceLocation4)).with(PropertyDispatch.property(BlockStateProperties.AXIS).select(Direction.Axis.X, Variant.variant().with(VariantProperties.MODEL, resourceLocation)).select(Direction.Axis.Y, Variant.variant().with(VariantProperties.MODEL, resourceLocation2)).select(Direction.Axis.Z, Variant.variant().with(VariantProperties.MODEL, resourceLocation3)));
    }

    public class BlockFamilyProvider {
        private final TextureMapping mapping;
        private final Map<ModelTemplate, ResourceLocation> models = Maps.newHashMap();
        @Nullable
        private BlockFamily family;
        @Nullable
        private ResourceLocation fullBlock;

        public BlockFamilyProvider(TextureMapping textureMapping) {
            this.mapping = textureMapping;
        }

        public BlockFamilyProvider fullBlock(Block block, ModelTemplate modelTemplate) {
            this.fullBlock = modelTemplate.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createSimpleBlock(block, this.fullBlock));
            return this;
        }

        public BlockFamilyProvider fullBlockCopies(Block... blocks) {
            if (this.fullBlock == null) {
                throw new IllegalStateException("Full block not generated yet");
            } else {
                Block[] var2 = blocks;
                int var3 = blocks.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    Block block = var2[var4];
                    BlockModelSubProvider.this.blockStateOutput.accept(createSimpleBlock(block, this.fullBlock));
                    BlockModelSubProvider.this.delegateItemModel(block, this.fullBlock);
                }

                return this;
            }
        }

        public BlockFamilyProvider button(Block block) {
            ResourceLocation resourceLocation = ModelTemplates.BUTTON.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation2 = ModelTemplates.BUTTON_PRESSED.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createButton(block, resourceLocation, resourceLocation2));
            ResourceLocation resourceLocation3 = ModelTemplates.BUTTON_INVENTORY.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.delegateItemModel(block, resourceLocation3);
            return this;
        }

        public BlockFamilyProvider wall(Block block) {
            ResourceLocation resourceLocation = ModelTemplates.WALL_POST.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation2 = ModelTemplates.WALL_LOW_SIDE.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation3 = ModelTemplates.WALL_TALL_SIDE.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createWall(block, resourceLocation, resourceLocation2, resourceLocation3));
            ResourceLocation resourceLocation4 = ModelTemplates.WALL_INVENTORY.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.delegateItemModel(block, resourceLocation4);
            return this;
        }

        public BlockFamilyProvider customFence(Block block) {
            TextureMapping textureMapping = TextureMapping.customParticle(block);
            ResourceLocation resourceLocation = ModelTemplates.CUSTOM_FENCE_POST.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation2 = ModelTemplates.CUSTOM_FENCE_SIDE_NORTH.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation3 = ModelTemplates.CUSTOM_FENCE_SIDE_EAST.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation4 = ModelTemplates.CUSTOM_FENCE_SIDE_SOUTH.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation5 = ModelTemplates.CUSTOM_FENCE_SIDE_WEST.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createCustomFence(block, resourceLocation, resourceLocation2, resourceLocation3, resourceLocation4, resourceLocation5));
            ResourceLocation resourceLocation6 = ModelTemplates.CUSTOM_FENCE_INVENTORY.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.delegateItemModel(block, resourceLocation6);
            return this;
        }

        public BlockFamilyProvider fence(Block block) {
            ResourceLocation resourceLocation = ModelTemplates.FENCE_POST.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation2 = ModelTemplates.FENCE_SIDE.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createFence(block, resourceLocation, resourceLocation2));
            ResourceLocation resourceLocation3 = ModelTemplates.FENCE_INVENTORY.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.delegateItemModel(block, resourceLocation3);
            return this;
        }

        public BlockFamilyProvider customFenceGate(Block block) {
            TextureMapping textureMapping = TextureMapping.customParticle(block);
            ResourceLocation resourceLocation = ModelTemplates.CUSTOM_FENCE_GATE_OPEN.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation2 = ModelTemplates.CUSTOM_FENCE_GATE_CLOSED.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation3 = ModelTemplates.CUSTOM_FENCE_GATE_WALL_OPEN.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation4 = ModelTemplates.CUSTOM_FENCE_GATE_WALL_CLOSED.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createFenceGate(block, resourceLocation, resourceLocation2, resourceLocation3, resourceLocation4, false));
            return this;
        }

        public BlockFamilyProvider fenceGate(Block block) {
            ResourceLocation resourceLocation = ModelTemplates.FENCE_GATE_OPEN.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation2 = ModelTemplates.FENCE_GATE_CLOSED.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation3 = ModelTemplates.FENCE_GATE_WALL_OPEN.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation4 = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createFenceGate(block, resourceLocation, resourceLocation2, resourceLocation3, resourceLocation4, true));
            return this;
        }

        public BlockFamilyProvider pressurePlate(Block block) {
            ResourceLocation resourceLocation = ModelTemplates.PRESSURE_PLATE_UP.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation2 = ModelTemplates.PRESSURE_PLATE_DOWN.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createPressurePlate(block, resourceLocation, resourceLocation2));
            return this;
        }

        public BlockFamilyProvider sign(Block block) {
            if (this.family == null) {
                throw new IllegalStateException("Family not defined");
            } else {
                Block block2 = this.family.getVariants().get(BlockFamily.Variant.WALL_SIGN);
                ResourceLocation resourceLocation = ModelTemplates.PARTICLE_ONLY.create(block, this.mapping, BlockModelSubProvider.this.modelOutput);
                BlockModelSubProvider.this.blockStateOutput.accept(createSimpleBlock(block, resourceLocation));
                BlockModelSubProvider.this.blockStateOutput.accept(createSimpleBlock(block2, resourceLocation));
                BlockModelSubProvider.this.createSimpleFlatItemModel(block.asItem());
                BlockModelSubProvider.this.skipAutoItemBlock(block2);
                return this;
            }
        }

        public BlockFamilyProvider slab(Block block) {
            if (this.fullBlock == null) {
                throw new IllegalStateException("Full block not generated yet");
            } else {
                ResourceLocation resourceLocation = this.getOrCreateModel(ModelTemplates.SLAB_BOTTOM, block);
                ResourceLocation resourceLocation2 = this.getOrCreateModel(ModelTemplates.SLAB_TOP, block);
                BlockModelSubProvider.this.blockStateOutput.accept(createSlab(block, resourceLocation, resourceLocation2, this.fullBlock));
                BlockModelSubProvider.this.delegateItemModel(block, resourceLocation);
                return this;
            }
        }

        public BlockFamilyProvider stairs(Block block) {
            ResourceLocation resourceLocation = this.getOrCreateModel(ModelTemplates.STAIRS_INNER, block);
            ResourceLocation resourceLocation2 = this.getOrCreateModel(ModelTemplates.STAIRS_STRAIGHT, block);
            ResourceLocation resourceLocation3 = this.getOrCreateModel(ModelTemplates.STAIRS_OUTER, block);
            BlockModelSubProvider.this.blockStateOutput.accept(createStairs(block, resourceLocation, resourceLocation2, resourceLocation3));
            BlockModelSubProvider.this.delegateItemModel(block, resourceLocation2);
            return this;
        }

        private BlockFamilyProvider fullBlockVariant(Block block) {
            TexturedModel texturedModel = BlockModelSubProvider.this.getTexturedModels().getOrDefault(block, TexturedModel.CUBE.get(block));
            BlockModelSubProvider.this.blockStateOutput.accept(createSimpleBlock(block, texturedModel.create(block, BlockModelSubProvider.this.modelOutput)));
            return this;
        }

        private BlockFamilyProvider door(Block block) {
            BlockModelSubProvider.this.createDoor(block);
            return this;
        }

        private void trapdoor(Block block) {
            if (BlockModelSubProvider.this.getNonOrientableTrapdoors().contains(block)) {
                BlockModelSubProvider.this.createTrapdoor(block);
            } else {
                BlockModelSubProvider.this.createOrientableTrapdoor(block);
            }

        }

        private ResourceLocation getOrCreateModel(ModelTemplate modelTemplate, Block block) {
            return this.models.computeIfAbsent(modelTemplate, (modelTemplatex) -> modelTemplatex.create(block, this.mapping, BlockModelSubProvider.this.modelOutput));
        }

        public BlockFamilyProvider generateFor(BlockFamily blockFamily) {
            this.family = blockFamily;
            blockFamily.getVariants().forEach((variant, block) -> {
                BiConsumer<BlockFamilyProvider, Block> biConsumer = SHAPE_CONSUMERS.get(variant);
                if (biConsumer != null) {
                    biConsumer.accept(this, block);
                }

            });
            return this;
        }
    }

    public class WoodProvider {
        private final TextureMapping logMapping;

        public WoodProvider(TextureMapping textureMapping) {
            this.logMapping = textureMapping;
        }

        public WoodProvider wood(Block block) {
            TextureMapping textureMapping = this.logMapping.copyAndUpdate(TextureSlot.END, this.logMapping.get(TextureSlot.SIDE));
            ResourceLocation resourceLocation = ModelTemplates.CUBE_COLUMN.create(block, textureMapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createAxisAlignedPillarBlock(block, resourceLocation));
            return this;
        }

        public WoodProvider log(Block block) {
            ResourceLocation resourceLocation = ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createAxisAlignedPillarBlock(block, resourceLocation));
            return this;
        }

        public WoodProvider logWithHorizontal(Block block) {
            ResourceLocation resourceLocation = ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelSubProvider.this.modelOutput);
            ResourceLocation resourceLocation2 = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, this.logMapping, BlockModelSubProvider.this.modelOutput);
            BlockModelSubProvider.this.blockStateOutput.accept(createRotatedPillarWithHorizontalVariant(block, resourceLocation, resourceLocation2));
            return this;
        }

        public WoodProvider logUVLocked(Block block) {
            BlockModelSubProvider.this.blockStateOutput.accept(createPillarBlockUVLocked(block, this.logMapping, BlockModelSubProvider.this.modelOutput));
            return this;
        }
    }

    @FunctionalInterface
    private interface BlockStateGeneratorSupplier {
        BlockStateGenerator create(Block block, ResourceLocation resourceLocation, TextureMapping textureMapping, BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer);
    }
}

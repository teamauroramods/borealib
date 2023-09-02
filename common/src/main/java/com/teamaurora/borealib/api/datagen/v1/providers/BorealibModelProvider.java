package com.teamaurora.borealib.api.datagen.v1.providers;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.teamaurora.borealib.api.block.v1.BorealibCeilingHangingSignBlock;
import com.teamaurora.borealib.api.block.v1.BorealibWallHangingSignBlock;
import com.teamaurora.borealib.api.block.v1.compat.BorealibChestBlock;
import com.teamaurora.borealib.api.block.v1.compat.BorealibTrappedChestBlock;
import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.api.datagen.v1.util.BorealibModelTemplates;
import com.teamaurora.borealib.api.datagen.v1.util.ModelGeneratorHelper;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author ebo2022
 * @author rose_
 * @since 1.0
 */
public abstract class BorealibModelProvider implements DataProvider {
    private final String domain;
    private final PackOutput.PathProvider blockStatePathProvider;
    private final PackOutput.PathProvider modelPathProvider;

    protected BorealibModelProvider(BorealibPackOutput output) {
        this.domain = output.getModId();
        this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
    }

    /**
     * Registers all block states and models to be generated.
     *
     * @param generators The generators to add block models to
     */
    public abstract void generateBlockModels(BlockModelGenerators generators);

    /**
     * Registers all item models to be generated.
     *
     * @param generators The generators to add item models to
     */
    public abstract void generateItemModels(ItemModelGenerators generators);

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Map<Block, BlockStateGenerator> blockStates = new HashMap<>();

        Consumer<BlockStateGenerator> blockStateOutput = (blockStateGenerator) -> {
            Block block = blockStateGenerator.getBlock();
            BlockStateGenerator blockState = blockStates.put(block, blockStateGenerator);
            if (blockState != null)
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
        };

        Map<ResourceLocation, Supplier<JsonElement>> models = new HashMap<>();
        Set<Item> skippedAutoModels = new HashSet<>();

        BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput = (resourceLocation, supplier) -> {
            Supplier<JsonElement> model = models.put(resourceLocation, supplier);
            if (model != null)
                throw new IllegalStateException("Duplicate model definition for " + resourceLocation);
        };

        Consumer<Item> skippedAutoModelsOutput = skippedAutoModels::add;
        this.generateBlockModels(new BlockModelGenerators(blockStateOutput, modelOutput, skippedAutoModelsOutput));
        this.generateItemModels(new ItemModelGenerators(modelOutput));

        RegistryWrapper.BLOCKS.forEach((block) -> {
            if (!this.domain.equals(RegistryWrapper.BLOCKS.getKey(block).getNamespace()))
                return;

            Item item = Item.BY_BLOCK.get(block);
            if (item != null) {
                if (skippedAutoModels.contains(item))
                    return;
                ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(item);
                if (!models.containsKey(modelLocation))
                    models.put(modelLocation, new DelegatedModel(ModelLocationUtils.getModelLocation(block)));
            }
        });
        return CompletableFuture.allOf(
                this.saveCollection(cachedOutput, blockStates, block -> this.blockStatePathProvider.json(block.builtInRegistryHolder().key().location())),
                this.saveCollection(cachedOutput, models, this.modelPathProvider::json)
        );
    }

    private <T> CompletableFuture<?> saveCollection(CachedOutput cachedOutput, Map<T, ? extends Supplier<JsonElement>> map, Function<T, Path> function) {
        return CompletableFuture.allOf(map.entrySet().stream().map((entry) -> {
            Path path = function.apply(entry.getKey());
            JsonElement jsonElement = entry.getValue().get();
            return DataProvider.saveStable(cachedOutput, jsonElement, path);
        }).toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Model Definitions";
    }

    // Util //

    protected static void createWoodFamily(BlockModelGenerators generator, BlockFamily planksFamily,
                                         RegistryReference<Block> log, RegistryReference<Block> wood,
                                         RegistryReference<Block> strippedLog, RegistryReference<Block> strippedWood,
                                         Pair<RegistryReference<BorealibChestBlock>, RegistryReference<BorealibTrappedChestBlock>> chests,
                                         RegistryReference<Block> bookshelf, RegistryReference<Block> cabinet,
                                         Pair<RegistryReference<BorealibCeilingHangingSignBlock>, RegistryReference<BorealibWallHangingSignBlock>> hangingSigns) {
        generator.family(planksFamily.getBaseBlock()).generateFor(planksFamily);
        generator.woodProvider(log.get()).logWithHorizontal(log.get()).wood(wood.get());
        generator.woodProvider(strippedLog.get()).logWithHorizontal(strippedLog.get()).wood(strippedWood.get());
        generator.blockEntityModels(chests.getFirst().get(), planksFamily.getBaseBlock()).createWithCustomBlockItemModel(BorealibModelTemplates.CHEST_ITEM, chests.getFirst().get(), chests.getSecond().get());
        createBookshelf(generator, bookshelf.get(), planksFamily.getBaseBlock());
        createCabinet(generator, cabinet.get());
        generator.createHangingSign(planksFamily.getBaseBlock(), hangingSigns.getFirst().get(), hangingSigns.getSecond().get());
    }

    protected static void createBookshelf(BlockModelGenerators generator, Block shelf, Block planks) {
        TextureMapping textureMapping = TextureMapping.column(TextureMapping.getBlockTexture(shelf), TextureMapping.getBlockTexture(planks));
        ResourceLocation modelLocation = ModelTemplates.CUBE_COLUMN.create(shelf, textureMapping, generator.modelOutput);
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(shelf, modelLocation));
    }

    protected static void createCabinet(BlockModelGenerators generator, Block cabinet) {
        ResourceLocation resourceLocation = TexturedModel.ORIENTABLE_ONLY_TOP.create(cabinet, generator.modelOutput);
        ResourceLocation openTexture = TextureMapping.getBlockTexture(cabinet, "_front_open");
        ResourceLocation resourceLocation3 = TexturedModel.ORIENTABLE_ONLY_TOP.get(cabinet).updateTextures((textureMapping) -> textureMapping.put(TextureSlot.FRONT, openTexture)).createWithSuffix(cabinet, "_open", generator.modelOutput);
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cabinet).with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.OPEN, resourceLocation3, resourceLocation)).with(BlockModelGenerators.createHorizontalFacingDispatch()));
    }

    protected static void createThatchFamily(BlockModelGenerators generator, BlockFamily family) {
        TextureSlot thatch_slot = ModelGeneratorHelper.slot("thatch");
        TextureSlot extrudes_slot = ModelGeneratorHelper.slot("extrudes");

        ModelTemplate thatch = ModelGeneratorHelper.template(new ResourceLocation("borealib:block/thatch/thatch"), thatch_slot, extrudes_slot);
        ModelTemplate thatch_slab = ModelGeneratorHelper.template(new ResourceLocation("borealib:block/thatch/thatch_slab"), thatch_slot, extrudes_slot);
        ModelTemplate thatch_slab_top = ModelGeneratorHelper.template(new ResourceLocation("borealib:block/thatch/thatch_slab_top"), thatch_slot, extrudes_slot);
        ModelTemplate thatch_stairs = ModelGeneratorHelper.template(new ResourceLocation("borealib:block/thatch/thatch_stairs"), thatch_slot, extrudes_slot);
        ModelTemplate thatch_stairs_top = ModelGeneratorHelper.template(new ResourceLocation("borealib:block/thatch/thatch_stairs_top"), thatch_slot, extrudes_slot);
        ModelTemplate thatch_stairs_inner = ModelGeneratorHelper.template(new ResourceLocation("borealib:block/thatch/thatch_stairs_inner"), thatch_slot, extrudes_slot);
        ModelTemplate thatch_stairs_inner_top = ModelGeneratorHelper.template(new ResourceLocation("borealib:block/thatch/thatch_stairs_inner_top"), thatch_slot, extrudes_slot);
        ModelTemplate thatch_stairs_outer = ModelGeneratorHelper.template(new ResourceLocation("borealib:block/thatch/thatch_stairs_outer"), thatch_slot, extrudes_slot);
        ModelTemplate thatch_stairs_outer_top = ModelGeneratorHelper.template(new ResourceLocation("borealib:block/thatch/thatch_stairs_outer_top"), thatch_slot, extrudes_slot);

        TextureMapping mapping = new TextureMapping().put(thatch_slot, TextureMapping.getBlockTexture(family.getBaseBlock())).put(extrudes_slot, TextureMapping.getBlockTexture(family.getBaseBlock(), "_extrudes"));
        ResourceLocation fullBlock = thatch.create(family.getBaseBlock(), mapping, generator.modelOutput);

        Block slabBlock = Objects.requireNonNull(family.get(BlockFamily.Variant.SLAB), "Family doesn't have a slab block");
        ResourceLocation slab = thatch_slab.create(slabBlock, mapping, generator.modelOutput);
        ResourceLocation slabTop = thatch_slab_top.create(ModelLocationUtils.getModelLocation(slabBlock, "_top"), mapping, generator.modelOutput);

        Block stairBlock = Objects.requireNonNull(family.get(BlockFamily.Variant.STAIRS), "Family doesn't have a stair block");
        ResourceLocation straight = thatch_stairs.create(stairBlock, mapping, generator.modelOutput);
        ResourceLocation straightTop = thatch_stairs_top.create(ModelLocationUtils.getModelLocation(stairBlock, "_top"), mapping, generator.modelOutput);
        ResourceLocation inner = thatch_stairs_inner.create(ModelLocationUtils.getModelLocation(stairBlock, "_inner"), mapping, generator.modelOutput);
        ResourceLocation innerTop = thatch_stairs_inner_top.create(ModelLocationUtils.getModelLocation(stairBlock, "_inner_top"), mapping, generator.modelOutput);
        ResourceLocation outer = thatch_stairs_outer.create(ModelLocationUtils.getModelLocation(stairBlock, "_outer"), mapping, generator.modelOutput);
        ResourceLocation outerTop = thatch_stairs_outer_top.create(ModelLocationUtils.getModelLocation(stairBlock, "_outer_top"), mapping, generator.modelOutput);

        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(stairBlock).with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.STAIRS_SHAPE)
                .select(Direction.EAST, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straight))
                .select(Direction.WEST, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straight).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .select(Direction.SOUTH, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straight).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(Direction.NORTH, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straight).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outer))
                .select(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .select(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outer))
                .select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outer).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, inner))
                .select(Direction.WEST, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .select(Direction.WEST, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, inner))
                .select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, inner).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .select(Direction.EAST, Half.TOP, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .select(Direction.WEST, Half.TOP, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightTop).with(VariantProperties.UV_LOCK, true))
                .select(Direction.SOUTH, Half.TOP, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .select(Direction.NORTH, Half.TOP, StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, straightTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(Direction.EAST, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerTop).with(VariantProperties.UV_LOCK, true))
                .select(Direction.WEST, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(Direction.NORTH, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .select(Direction.EAST, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .select(Direction.WEST, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerTop).with(VariantProperties.UV_LOCK, true))
                .select(Direction.NORTH, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .select(Direction.EAST, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(Direction.WEST, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
                .select(Direction.SOUTH, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .select(Direction.NORTH, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerTop).with(VariantProperties.UV_LOCK, true))
                .select(Direction.EAST, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerTop).with(VariantProperties.UV_LOCK, true))
                .select(Direction.WEST, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .select(Direction.SOUTH, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .select(Direction.NORTH, Half.TOP, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerTop).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true))
        ));
        generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(family.getBaseBlock(), fullBlock));
        generator.blockStateOutput.accept(BlockModelGenerators.createSlab(slabBlock, slab, slabTop, fullBlock));
    }

}

package com.teamaurora.borealib.api.datagen.v1.providers;

import com.google.gson.JsonElement;
import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author ebo2022
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
}

package com.teamaurora.magnetosphere.api.datagen.v1.providers.model;

import com.google.gson.JsonElement;
import com.teamaurora.magnetosphere.api.base.v1.platform.ModContainer;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
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

public class MagnetosphereModelProvider implements DataProvider {

    private final PackOutput.PathProvider blockStatePathProvider;
    private final PackOutput.PathProvider modelPathProvider;
    private final Set<ModelGeneratorFactory> factories;
    private final String domain;

    public MagnetosphereModelProvider(PackOutput output, ModContainer container) {
        this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
        this.factories = new HashSet<>();
        this.domain = container.getId();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<Block, BlockStateGenerator> blockStates = new HashMap<>();
        Consumer<BlockStateGenerator> blockStateOutput = (blockStateGenerator) -> {
            Block block = blockStateGenerator.getBlock();
            BlockStateGenerator blockState = blockStates.put(block, blockStateGenerator);
            if (blockState != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        };
        Map<ResourceLocation, Supplier<JsonElement>> models = new HashMap<>();
        Set<Item> skippedAutoModels = new HashSet<>();
        BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput = (resourceLocation, supplier) -> {
            Supplier<JsonElement> model = models.put(resourceLocation, supplier);
            if (model != null) {
                throw new IllegalStateException("Duplicate model definition for " + resourceLocation);
            }
        };
        Consumer<Item> skippedAutoModelsOutput = skippedAutoModels::add;
        this.factories.stream().map(factory -> factory.create(blockStateOutput, modelOutput, skippedAutoModelsOutput)).forEach(ModelSubProvider::run);
        RegistryView.BLOCK.forEach((block) -> {
            if (!this.domain.equals(Objects.requireNonNull(RegistryView.BLOCK.getKey(block)).getNamespace()))
                return;

            Item item = Item.BY_BLOCK.get(block);
            if (item != null) {
                if (skippedAutoModels.contains(item))
                    return;
                ResourceLocation itemLocation = ModelLocationUtils.getModelLocation(item);
                if (!models.containsKey(itemLocation))
                    models.put(itemLocation, new DelegatedModel(ModelLocationUtils.getModelLocation(block)));
            }
        });
        CompletableFuture<?>[] futures = new CompletableFuture<?>[]{
                this.saveCollection(cache, blockStates, (block) -> this.blockStatePathProvider.json(block.builtInRegistryHolder().key().location())),
                this.saveCollection(cache, models, this.modelPathProvider::json)
        };
        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getName() {
        return "Block State Definitions";
    }

    private <T> CompletableFuture<?> saveCollection(CachedOutput cache, Map<T, ? extends Supplier<JsonElement>> objectToJsonMap, Function<T, Path> function) {
        return CompletableFuture.allOf(objectToJsonMap.entrySet().stream().map((entry) -> {
            Path path = function.apply(entry.getKey());
            JsonElement jsonElement = entry.getValue().get();
            return DataProvider.saveStable(cache, jsonElement, path);
        }).toArray(CompletableFuture[]::new));
    }

    @FunctionalInterface
    public interface ModelGeneratorFactory {
        ModelSubProvider create(Consumer<BlockStateGenerator> blockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput, Consumer<Item> skippedAutoModelsOutput);
    }
}

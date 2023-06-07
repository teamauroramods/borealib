package com.teamaurora.borealib.api.datagen.v1.providers.model;

import com.google.gson.JsonElement;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class ItemModelSubProvider implements ModelSubProvider {

    protected final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;

    public ItemModelSubProvider(BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
        this.modelOutput = modelOutput;
    }

    public BiConsumer<ResourceLocation, Supplier<JsonElement>> getModelOutput() {
        return modelOutput;
    }

    protected void generateFlatItem(Item item, ModelTemplate modelTemplate) {
        modelTemplate.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.modelOutput);
    }

    protected void generateFlatItem(Item item, String modelLocationSuffix, ModelTemplate modelTemplate) {
        modelTemplate.create(ModelLocationUtils.getModelLocation(item, modelLocationSuffix), TextureMapping.layer0(TextureMapping.getItemTexture(item, modelLocationSuffix)), this.modelOutput);
    }

    protected void generateFlatItem(Item item, Item layerZeroItem, ModelTemplate modelTemplate) {
        modelTemplate.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(layerZeroItem), this.modelOutput);
    }

    protected void createSimpleFlatItemModel(Block flatBlock) {
        Item item = flatBlock.asItem();
        if (item != Items.AIR)
            ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(flatBlock), this.modelOutput);
    }
}

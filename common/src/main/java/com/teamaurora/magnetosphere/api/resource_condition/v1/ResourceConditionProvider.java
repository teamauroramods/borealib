package com.teamaurora.magnetosphere.api.resource_condition.v1;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface ResourceConditionProvider {

    void write(JsonObject json);

    ResourceLocation getName();

    static void write(JsonObject conditionalObject, ResourceConditionProvider... conditions) {
    }
}
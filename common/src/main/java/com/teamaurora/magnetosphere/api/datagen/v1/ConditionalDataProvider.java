package com.teamaurora.magnetosphere.api.datagen.v1;

import com.google.gson.JsonObject;
import com.teamaurora.magnetosphere.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

public interface ConditionalDataProvider extends DataProvider {

    void addConditions(ResourceLocation id, ResourceConditionProvider... providers);

    void injectConditions(ResourceLocation id, JsonObject json);
}

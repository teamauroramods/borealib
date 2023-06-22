package com.teamaurora.borealib.api.resource_condition.v1;

import com.google.gson.JsonObject;
import com.teamaurora.borealib.impl.resource_condition.ResourceConditionProviderImpl;
import net.minecraft.resources.ResourceLocation;

public interface ResourceConditionProvider {

    void write(JsonObject json);

    ResourceLocation getName();

    static void write(JsonObject conditionalObject, ResourceConditionProvider... conditions) {
        ResourceConditionProviderImpl.write(conditionalObject, conditions);
    }
}
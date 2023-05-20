package com.teamaurora.magnetosphere.impl.resource_condition.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.teamaurora.magnetosphere.api.resource_condition.v1.ResourceConditionProvider;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ResourceConditionProviderImplImpl {

    public static void write(JsonObject conditionalObject, ResourceConditionProvider... conditions) {
        if (conditions.length == 0)
            return;
        if (conditionalObject.has("conditions"))
            throw new IllegalArgumentException("Object already has a condition entry: " + conditionalObject);
        JsonArray conditionsJson = new JsonArray();
        for (ResourceConditionProvider condition : conditions) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", condition.getName().toString());
            condition.write(jsonObject);
            conditionsJson.add(jsonObject);
        }
        conditionalObject.add("conditions", conditionsJson);
    }
}

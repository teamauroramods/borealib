package com.teamaurora.magnetosphere.api.resource_condition.v1;

import com.google.gson.JsonObject;
import com.teamaurora.magnetosphere.impl.resource_condition.ResourceConditionRegistryImpl;
import net.minecraft.resources.ResourceLocation;

public interface ResourceConditionRegistry {

    static void register(ResourceLocation id, ResourceCondition condition) {
        ResourceConditionRegistryImpl.register(id, condition);
    }

    static boolean test(JsonObject json) {
        return ResourceConditionRegistryImpl.test(json);
    }

    static String getConditionsKey() {
        return ResourceConditionRegistryImpl.getConditionsKey();
    }
}

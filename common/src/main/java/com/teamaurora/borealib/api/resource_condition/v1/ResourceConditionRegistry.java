package com.teamaurora.borealib.api.resource_condition.v1;

import com.google.gson.JsonObject;
import com.teamaurora.borealib.impl.resource_condition.ResourceConditionRegistryImpl;
import net.minecraft.resources.ResourceLocation;

public interface ResourceConditionRegistry {

    static void register(ResourceLocation id, ResourceCondition condition) {
        ResourceConditionRegistryImpl.register(id, condition);
    }

    static ResourceCondition getCondition(JsonObject json) {
        return ResourceConditionRegistryImpl.getCondition(json);
    }

    static boolean test(JsonObject json) {
        return ResourceConditionRegistryImpl.test(json);
    }

    static String getConditionsKey() {
        return ResourceConditionRegistryImpl.getConditionsKey();
    }
}

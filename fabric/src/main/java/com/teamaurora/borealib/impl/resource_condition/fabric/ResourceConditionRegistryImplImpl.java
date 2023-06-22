package com.teamaurora.borealib.impl.resource_condition.fabric;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

@ApiStatus.Internal
public class ResourceConditionRegistryImplImpl {

    public static void register(ResourceLocation name, ResourceCondition condition) {
        ResourceConditions.register(name, condition::test);
    }

    public static boolean test(JsonObject json) {
        return ResourceConditions.objectMatchesConditions(json);
    }

    public static String getConditionsKey() {
        return ResourceConditions.CONDITIONS_KEY;
    }

    public static ResourceCondition getCondition(JsonObject json) {
        return json1 -> Objects.requireNonNull(ResourceConditions.get(new ResourceLocation(GsonHelper.getAsString(json1, getConditionsKey())))).test(json1);
    }
}

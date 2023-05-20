package com.teamaurora.magnetosphere.impl.resource_condition.fabric;

import com.google.gson.JsonObject;
import com.teamaurora.magnetosphere.api.resource_condition.v1.ResourceConditionProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

@ApiStatus.Internal
public class ResourceConditionProviderImplImpl {

    public static void write(JsonObject conditionalObject, ResourceConditionProvider... conditions) {
        ConditionJsonProvider.write(conditionalObject, conditions.length == 0 ? null : Arrays.stream(conditions).map(DefaultResourceConditionsImplImpl::wrap).toArray(ConditionJsonProvider[]::new));
    }
}

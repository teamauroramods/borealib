package com.teamaurora.magnetosphere.api.datagen.v1;

import com.google.gson.JsonObject;
import com.teamaurora.magnetosphere.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public abstract class SimpleConditionalDataProvider implements ConditionalDataProvider {

    private final Map<ResourceLocation, List<ResourceConditionProvider>> providers;

    protected SimpleConditionalDataProvider() {
        this.providers = new HashMap<>();
    }

    @Override
    public void addConditions(ResourceLocation id, ResourceConditionProvider... providers) {
        if (providers.length == 0)
            return;
        this.providers.computeIfAbsent(id, __ -> new ArrayList<>()).addAll(Arrays.asList(providers));
    }

    @Override
    public void injectConditions(ResourceLocation id, JsonObject json) {
        if (this.providers.containsKey(id))
            ResourceConditionProvider.write(json, this.providers.get(id).toArray(new ResourceConditionProvider[0]));
    }
}

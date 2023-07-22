package com.teamaurora.borealib.impl.resource_condition;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceCondition;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class RegistryKeyExistsResourceCondition implements ResourceCondition {

    public static final ResourceLocation NAME = Borealib.location("registry_key_exists");

    @Override
    public boolean test(JsonObject json) throws JsonParseException {
        ResourceLocation registryId = new ResourceLocation(GsonHelper.getAsString(json, "registry"));
        ResourceLocation key = new ResourceLocation(GsonHelper.getAsString(json, "key"));
        RegistryWrapper<?> maybeRegistry = RegistryWrapper.getById(registryId);
        if (maybeRegistry != null) {
            return maybeRegistry.containsKey(key);
        } else {
            Registry<?> dynamicRegistry = Platform.getRegistryAccess().flatMap(r -> r.registry(ResourceKey.createRegistryKey(registryId))).orElse(null);
            return dynamicRegistry != null && dynamicRegistry.containsKey(key);
        }
    }
}
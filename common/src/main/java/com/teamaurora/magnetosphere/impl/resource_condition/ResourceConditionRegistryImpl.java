package com.teamaurora.magnetosphere.impl.resource_condition;

import com.google.gson.JsonObject;
import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.api.resource_condition.v1.ResourceCondition;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ResourceConditionRegistryImpl {

    @ExpectPlatform
    public static void register(ResourceLocation name, ResourceCondition condition) {
        Platform.expect();
    }

    @ExpectPlatform
    public static boolean test(JsonObject json) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static String getConditionsKey() {
        return Platform.expect();
    }
}

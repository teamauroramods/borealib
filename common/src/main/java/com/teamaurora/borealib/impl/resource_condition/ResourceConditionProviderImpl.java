package com.teamaurora.borealib.impl.resource_condition;

import com.google.gson.JsonObject;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ResourceConditionProviderImpl {

    @ExpectPlatform
    public static void write(JsonObject conditionalObject, ResourceConditionProvider... conditions) {
        Platform.expect();
    }
}

package com.teamaurora.magnetosphere.impl.resource_condition;

import com.google.gson.JsonObject;
import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.api.base.v1.util.NumberComparator;
import com.teamaurora.magnetosphere.api.config.v1.ModConfig;
import com.teamaurora.magnetosphere.api.resource_condition.v1.ResourceConditionProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class DefaultResourceConditionsImpl {

    @ExpectPlatform
    public static ResourceConditionProvider and(ResourceConditionProvider... values) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static ResourceConditionProvider FALSE() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static ResourceConditionProvider TRUE() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static ResourceConditionProvider not(ResourceConditionProvider value) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static ResourceConditionProvider or(ResourceConditionProvider... values) {
        return Platform.expect();
    }

    public static ResourceConditionProvider registryKeyExists(ResourceKey<? extends Registry<?>> registry, ResourceLocation key) {
        return new ResourceConditionProvider() {
            @Override
            public ResourceLocation getName() {
                return RegistryKeyExistsResourceCondition.NAME;
            }

            @Override
            public void write(JsonObject json) {
                json.addProperty("registry", registry.location().toString());
                json.addProperty("key", key.toString());
            }
        };
    }

    @ExpectPlatform
    public static ResourceConditionProvider allModsLoaded(String... modIds) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static ResourceConditionProvider anyModsLoaded(String... modIds) {
        return Platform.expect();
    }

    public static ResourceConditionProvider config(String modId, ModConfig.Type type, String key, Object value) {
        return new ConfigResourceCondition.SimpleProvider(modId, type, key, value);
    }

    public static ResourceConditionProvider config(String modId, ModConfig.Type type, String key, Number value, NumberComparator comparator) {
        return new ConfigResourceCondition.NumberProvider(modId, type, key, value, comparator);
    }
}

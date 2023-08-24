package com.teamaurora.borealib.impl.resource_condition;

import com.google.gson.JsonObject;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.base.v1.util.NumberComparator;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import com.teamaurora.borealib.core.Borealib;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class DefaultResourceConditionsImpl {

    public static final ResourceLocation QUARK_FLAG = Borealib.location("quark_flag");
    public static final ResourceLocation WOODWORKS_FLAG = Borealib.location("woodworks_flag");

    @ExpectPlatform
    public static ResourceConditionProvider configure(ResourceConditionProvider provider) {
        return Platform.expect();
    }

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
        return configure(new ResourceConditionProvider() {
            @Override
            public ResourceLocation getName() {
                return RegistryKeyExistsResourceCondition.NAME;
            }

            @Override
            public void write(JsonObject json) {
                json.addProperty("registry", registry.location().toString());
                json.addProperty("key", key.toString());
            }
        });
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
        return configure(new ConfigResourceCondition.SimpleProvider(modId, type, key, value));
    }

    public static ResourceConditionProvider config(String modId, ModConfig.Type type, String key, Number value, NumberComparator comparator) {
        return configure(new ConfigResourceCondition.NumberProvider(modId, type, key, value, comparator));
    }

    public static ResourceConditionProvider quarkFlag(String flag) {
        return configure(new ResourceConditionProvider() {
            @Override
            public void write(JsonObject json) {
                json.addProperty("flag", flag);
            }

            @Override
            public ResourceLocation getName() {
                return QUARK_FLAG;
            }
        });
    }

    public static ResourceConditionProvider woodworksFlag(String value) {
        return configure(new ResourceConditionProvider() {
            @Override
            public void write(JsonObject json) {
                json.addProperty("value", value);
            }

            @Override
            public ResourceLocation getName() {
                return WOODWORKS_FLAG;
            }
        });
    }
}

package com.teamaurora.borealib.impl.resource_condition.fabric;

import com.google.gson.JsonObject;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import com.teamaurora.borealib.core.Borealib;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

@ApiStatus.Internal
public class DefaultResourceConditionsImplImpl {

    private static final ResourceLocation TRUE = Borealib.location("true");
    private static final ResourceLocation FALSE = Borealib.location("false");
    private static final ResourceConditionProvider TRUE_CONDITION = wrap(new ConditionJsonProvider() {
        @Override
        public ResourceLocation getConditionId() {
            return TRUE;
        }

        @Override
        public void writeParameters(JsonObject object) {
        }
    });
    private static final ResourceConditionProvider FALSE_CONDITION = wrap(new ConditionJsonProvider() {
        @Override
        public ResourceLocation getConditionId() {
            return FALSE;
        }

        @Override
        public void writeParameters(JsonObject object) {
        }
    });

    public static void init() {
        ResourceConditions.register(TRUE, json -> true);
        ResourceConditions.register(FALSE, json -> false);
    }

    public static ResourceConditionProvider and(ResourceConditionProvider... values) {
        return wrap(DefaultResourceConditions.and(Arrays.stream(values).map(DefaultResourceConditionsImplImpl::wrap).toArray(ConditionJsonProvider[]::new)));
    }

    public static ResourceConditionProvider FALSE() {
        return FALSE_CONDITION;
    }

    public static ResourceConditionProvider TRUE() {
        return TRUE_CONDITION;
    }

    public static ResourceConditionProvider not(ResourceConditionProvider value) {
        return wrap(DefaultResourceConditions.not(wrap(value)));
    }

    public static ResourceConditionProvider or(ResourceConditionProvider... values) {
        return wrap(DefaultResourceConditions.or(Arrays.stream(values).map(DefaultResourceConditionsImplImpl::wrap).toArray(ConditionJsonProvider[]::new)));
    }

    public static ResourceConditionProvider allModsLoaded(String... modIds) {
        return wrap(DefaultResourceConditions.allModsLoaded(modIds));
    }

    public static ResourceConditionProvider anyModsLoaded(String... modIds) {
        return wrap(DefaultResourceConditions.anyModLoaded(modIds));
    }

    public static ResourceConditionProvider wrap(ConditionJsonProvider condition) {
        return new ResourceConditionWrapper(condition);
    }

    public static ConditionJsonProvider wrap(ResourceConditionProvider condition) {
        return new ConditionJsonWrapper(condition);
    }

    public static ResourceConditionProvider configure(ResourceConditionProvider provider) {
        return provider;
    }

    private record ConditionJsonWrapper(ResourceConditionProvider provider) implements ConditionJsonProvider {

        @Override
        public ResourceLocation getConditionId() {
            return this.provider.getName();
        }

        @Override
        public void writeParameters(JsonObject object) {
            this.provider.write(object);
        }
    }

    private record ResourceConditionWrapper(ConditionJsonProvider condition) implements ResourceConditionProvider {

        @Override
        public void write(JsonObject json) {
            this.condition.writeParameters(json);
        }

        @Override
        public ResourceLocation getName() {
            return this.condition.getConditionId();
        }
    }
}
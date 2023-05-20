package com.teamaurora.magnetosphere.impl.resource_condition.forge;

import com.google.gson.JsonObject;
import com.teamaurora.magnetosphere.api.resource_condition.v1.ResourceCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ResourceConditionWrapper implements ICondition {

    private final ResourceLocation name;
    private final String stringName;
    private final boolean result;

    public ResourceConditionWrapper(ResourceLocation name, String stringName, boolean result) {
        this.name = name;
        this.stringName = stringName;
        this.result = result;
    }

    @Override
    public ResourceLocation getID() {
        return name;
    }

    @Override
    public boolean test(IContext context) {
        return this.result;
    }

    @Override
    public String toString() {
        return stringName;
    }

    public static class Serializer implements IConditionSerializer<ResourceConditionWrapper> {

        private final ResourceLocation name;
        private final ResourceCondition condition;

        public Serializer(ResourceLocation name, ResourceCondition condition) {
            this.name = name;
            this.condition = condition;
        }

        @Override
        public void write(JsonObject json, ResourceConditionWrapper value) {
        }

        @Override
        public ResourceConditionWrapper read(JsonObject json) {
            return new ResourceConditionWrapper(this.name, this.condition.toString(), this.condition.test(json));
        }

        @Override
        public ResourceLocation getID() {
            return name;
        }

        public ResourceCondition getCondition() {
            return condition;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Serializer that = (Serializer) o;
            return this.name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }
    }
}
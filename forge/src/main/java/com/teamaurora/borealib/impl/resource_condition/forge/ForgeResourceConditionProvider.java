package com.teamaurora.borealib.impl.resource_condition.forge;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Field;
import java.util.Map;

@ApiStatus.Internal
@SuppressWarnings("unchecked")
public class ForgeResourceConditionProvider<T extends ICondition> implements ResourceConditionProvider {

    private static final Map<ResourceLocation, IConditionSerializer<?>> CONDITIONS;

    static {
        try {
            Field field = CraftingHelper.class.getDeclaredField("conditions");
            field.setAccessible(true);
            CONDITIONS = (Map<ResourceLocation, IConditionSerializer<?>>) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load conditions", e);
        }
    }

    private final T condition;

    public ForgeResourceConditionProvider(T condition) {
        this.condition = condition;
    }

    public T getCondition() {
        return this.condition;
    }

    @Override
    public void write(JsonObject json) {
        write(json, this.condition);
    }

    @Override
    public ResourceLocation getName() {
        return this.condition.getID();
    }

    private static <T extends ICondition> void write(JsonObject json, T condition) {
        // Exists to retain information of wrapped IConditions that would otherwise be lost in conversion to CustomWrapper, resulting in a ClassCastException
        if (condition instanceof DefaultResourceConditionsImplImpl.DelegatedWrapper<?> wrapper) {
            IConditionSerializer<?> serializer = CONDITIONS.get(wrapper.getID());
            if (serializer == null)
                throw new JsonSyntaxException("Unknown condition type: " + wrapper.getID().toString());
            wrapper.writeTo(serializer, json);
        } else {
            IConditionSerializer<T> serializer = (IConditionSerializer<T>) CONDITIONS.get(condition.getID());
            if (serializer == null)
                throw new JsonSyntaxException("Unknown condition type: " + condition.getID().toString());
            serializer.write(json, condition);
        }
    }
}
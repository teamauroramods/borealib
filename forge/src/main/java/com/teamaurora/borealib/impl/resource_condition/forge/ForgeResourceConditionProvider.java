package com.teamaurora.borealib.impl.resource_condition.forge;

import com.google.gson.JsonObject;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Field;
import java.util.Map;

@ApiStatus.Internal
public class ForgeResourceConditionProvider implements ResourceConditionProvider {

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

    private final ICondition condition;

    public ForgeResourceConditionProvider(ICondition condition) {
        this.condition = condition;
    }

    @Override
    public void write(JsonObject json) {
        write(json, this.condition);
    }

    @Override
    public ResourceLocation getName() {
        return this.condition.getID();
    }

    @SuppressWarnings("unchecked")
    private static <T extends ICondition> void write(JsonObject json, T condition) {
        IConditionSerializer<T> serializer = (IConditionSerializer<T>) CONDITIONS.get(condition.getID());
        serializer.write(json, condition);
    }
}
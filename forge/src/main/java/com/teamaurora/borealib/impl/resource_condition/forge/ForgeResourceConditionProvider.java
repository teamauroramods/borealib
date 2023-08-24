package com.teamaurora.borealib.impl.resource_condition.forge;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

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

    private final Either<T, ResourceConditionProvider> source;

    public ForgeResourceConditionProvider(T condition) {
        this.source = Either.left(condition);
    }

    public ForgeResourceConditionProvider(ResourceConditionProvider parent) {
        this.source = Either.right(parent);
    }

    public Either<T, ResourceConditionProvider> getSource() {
        return source;
    }

    @Override
    public void write(JsonObject json) {
        if (this.source.right().isPresent()) {
            // Custom-created provider, we don't want to mess with that
            this.source.right().get().write(json);
        } else {
           ICondition condition = this.source.left().get();
           if (condition instanceof DefaultResourceConditionsImplImpl.ConditionBasedWrapper<?> wrapper) {
               IConditionSerializer<?> serializer = CONDITIONS.get(wrapper.getID());
               if (serializer == null)
                   throw new JsonSyntaxException("Unknown condition type: " + wrapper.getID().toString());
               wrapper.writeTo(serializer, json);
               json.addProperty("type", wrapper.getID().toString());
           } else if (condition instanceof DefaultResourceConditionsImplImpl.ProviderBasedWrapper wrapper) {
               wrapper.value().write(json);
               json.addProperty("type", wrapper.getID().toString());
           } else {
               // Most likely the direct list of conditions; don't mess with this either
               IConditionSerializer<T> serializer = (IConditionSerializer<T>) CONDITIONS.get(condition.getID());
               if (serializer == null)
                   throw new JsonSyntaxException("Unknown condition type: " + condition.getID().toString());
               serializer.write(json, (T) condition);
           }
        }
    }

    @Override
    public ResourceLocation getName() {
        if (this.source.left().isPresent())
            return this.source.left().get().getID();
        else
            return this.source.right().get().getName();
    }
}
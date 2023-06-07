package com.teamaurora.borealib.impl.resource_condition.forge;

import com.google.gson.JsonObject;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceCondition;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
public class ResourceConditionRegistryImplImpl {

    private static final Set<IConditionSerializer<?>> CONDITIONS = ConcurrentHashMap.newKeySet();
    private static final String CONDITIONS_KEY = "conditions";

    public static void register(ResourceLocation name, ResourceCondition condition) {
        if (!CONDITIONS.add(new ResourceConditionWrapper.Serializer(name, condition)))
            Borealib.LOGGER.warn("Duplicate resource condition with id: " + name);
    }

    @SubscribeEvent
    public static void registerRecipeConditions(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
            CONDITIONS.forEach(CraftingHelper::register);
        }
    }

    public static IConditionSerializer<?> get(ResourceCondition condition) {
        return CONDITIONS.stream().filter(serializer -> serializer instanceof ResourceConditionWrapper.Serializer && ((ResourceConditionWrapper.Serializer) serializer).getCondition() == condition).findFirst().orElseThrow(() -> new IllegalStateException("Unregistered condition: " + condition));
    }

    public static boolean test(JsonObject json) {
        return CraftingHelper.processConditions(json, CONDITIONS_KEY, ICondition.IContext.EMPTY);
    }

    public static String getConditionsKey() {
        return CONDITIONS_KEY;
    }
}
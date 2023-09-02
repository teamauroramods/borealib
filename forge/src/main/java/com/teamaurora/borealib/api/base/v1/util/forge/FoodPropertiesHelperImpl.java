package com.teamaurora.borealib.api.base.v1.util.forge;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.function.Supplier;

public class FoodPropertiesHelperImpl {
    public static FoodProperties.Builder effect(FoodProperties.Builder properties, Supplier<MobEffectInstance> instance, float chance) {
        return properties.effect(instance, chance);
    }
}

package com.teamaurora.borealib.api.base.v1.util;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import java.util.function.Supplier;

public class FoodPropertiesHelper {
    @ExpectPlatform
    public static FoodProperties.Builder effect(FoodProperties.Builder properties, Supplier<MobEffectInstance> instance, float chance) {
        return Platform.expect();
    }

}

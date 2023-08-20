package com.teamaurora.borealib.core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.teamaurora.borealib.core.registry.BorealibEntityTypes;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityType.class)
public class EntityTypeMixin {

    @ModifyReturnValue(method = "getDescriptionId", at = @At("RETURN"))
    private String getDescriptionId(String original) {
        if ((EntityType<?>) (Object) this == BorealibEntityTypes.BOAT.get()) {
            return "entity.minecraft.boat";
        } else if ((EntityType<?>) (Object) this == BorealibEntityTypes.CHEST_BOAT.get()) {
            return "entity.minecraft.chest_boat";
        }
        return original;
    }
}

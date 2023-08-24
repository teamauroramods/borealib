package com.teamaurora.borealib.core.mixin.forge;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.teamaurora.borealib.impl.resource_condition.forge.DefaultResourceConditionsImplImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(CraftingHelper.class)
public class CraftingHelperMixin {

    @Shadow(remap = false)
    @Final
    private static Map<ResourceLocation, IConditionSerializer<?>> conditions;

    @Inject(method = "serialize(Lnet/minecraftforge/common/crafting/conditions/ICondition;)Lcom/google/gson/JsonObject;", at = @At("HEAD"), cancellable = true, remap = false)
    private static <T extends ICondition> void serialize(T condition, CallbackInfoReturnable<JsonObject> cir) {
        if (condition instanceof DefaultResourceConditionsImplImpl.DelegatedWrapper<?> wrapper) {
            IConditionSerializer<?> serializer = conditions.get(wrapper.getID());
            if (serializer == null)
                throw new JsonSyntaxException("Unknown condition type: " + wrapper.getID().toString());
            JsonObject json = new JsonObject();
            wrapper.writeTo(serializer, json);
            cir.setReturnValue(json);
        }
    }
}

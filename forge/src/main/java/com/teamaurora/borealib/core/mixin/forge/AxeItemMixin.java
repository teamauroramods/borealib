package com.teamaurora.borealib.core.mixin.forge;

import com.teamaurora.borealib.impl.content_registries.StrippingRegistryImpl;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(method = "getAxeStrippingState", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getAxeStrippingState(BlockState state, CallbackInfoReturnable<BlockState> cir) {
        BlockState strippedState = StrippingRegistryImpl.get(state);
        if (strippedState != null)
            cir.setReturnValue(strippedState);
    }
}
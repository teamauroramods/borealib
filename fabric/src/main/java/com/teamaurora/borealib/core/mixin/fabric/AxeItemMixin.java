package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.impl.content_registries.StrippingRegistryImpl;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(method = "getStripped", at = @At(value = "RETURN"), cancellable = true)
    public void useOn(BlockState blockState, CallbackInfoReturnable<Optional<BlockState>> cir) {
        BlockState state = StrippingRegistryImpl.get(blockState);
        if (state != null)
            cir.setReturnValue(Optional.of(state));
    }
}
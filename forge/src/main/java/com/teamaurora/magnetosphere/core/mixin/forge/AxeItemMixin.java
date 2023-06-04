package com.teamaurora.magnetosphere.core.mixin.forge;

import com.teamaurora.magnetosphere.api.content_registries.v1.BlockContentRegistries;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(method = "getAxeStrippingState", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getAxeStrippingState(BlockState blockState, CallbackInfoReturnable<BlockState> cir) {
        Block block = BlockContentRegistries.STRIPPING.get(blockState.getBlock());
        if (block != null)
            cir.setReturnValue(block.defaultBlockState().setValue(BlockStateProperties.AXIS, blockState.getValue(BlockStateProperties.AXIS)));
    }
}

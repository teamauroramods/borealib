package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.api.content_registries.v1.BlockContentRegistries;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(method = "getStripped", at = @At("HEAD"), cancellable = true)
    private void getStripped(BlockState blockState, CallbackInfoReturnable<Optional<BlockState>> cir) {
        Block block = BlockContentRegistries.STRIPPING.get(blockState.getBlock());
        if (block != null)
            cir.setReturnValue(Optional.of(block.defaultBlockState().setValue(BlockStateProperties.AXIS, blockState.getValue(BlockStateProperties.AXIS))));
    }
}

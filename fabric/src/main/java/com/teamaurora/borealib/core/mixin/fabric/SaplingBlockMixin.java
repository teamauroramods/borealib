package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.api.event.world.v1.WorldEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin {

    @Inject(method = "advanceTree", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/grower/AbstractTreeGrower;growTree(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource rand, CallbackInfo ci) {
        InteractionResult result = WorldEvents.TREE_GROWING.invoker().interaction(level, rand, pos);
        if (result == InteractionResult.FAIL)
            ci.cancel();
    }
}
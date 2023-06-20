package com.teamaurora.borealib.api.block.v1.entity.compat;

import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BorealibTrappedChestBlockEntity extends BorealibChestBlockEntity {

    public BorealibTrappedChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BorealibBlockEntityTypes.TRAPPED_CHEST.get(), blockPos, blockState);
    }

    @Override
    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int oldOpenCount, int openCount) {
        super.signalOpenCount(level, pos, state, oldOpenCount, openCount);
        if (oldOpenCount != openCount) {
            Block block = state.getBlock();
            level.updateNeighborsAt(pos, block);
            level.updateNeighborsAt(pos.below(), block);
        }
    }
}

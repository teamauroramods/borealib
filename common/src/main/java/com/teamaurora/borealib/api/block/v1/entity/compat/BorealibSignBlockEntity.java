package com.teamaurora.borealib.api.block.v1.entity.compat;

import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A sign block entity used for Borealib's sign blocks.\
 *
 * @author ebo2022
 * @since 1.0
 */
public class BorealibSignBlockEntity extends SignBlockEntity {

    public BorealibSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BorealibBlockEntityTypes.SIGN.get();
    }
}

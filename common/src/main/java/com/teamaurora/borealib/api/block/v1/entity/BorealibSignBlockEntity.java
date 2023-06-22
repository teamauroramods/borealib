package com.teamaurora.borealib.api.block.v1.entity;

import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

/**
 * A sign block entity used for Borealib's sign blocks.
 *
 * @author ebo2022
 * @since 1.0
 */
public class BorealibSignBlockEntity extends SignBlockEntity {

    public static final Set<Block> VALID_BLOCKS = new HashSet<>();

    public BorealibSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BorealibBlockEntityTypes.SIGN.get();
    }
}

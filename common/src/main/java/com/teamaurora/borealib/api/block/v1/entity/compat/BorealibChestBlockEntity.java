package com.teamaurora.borealib.api.block.v1.entity.compat;

import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A chest block entity used for Borealib's chest blocks. Based on Blueprint's chest system and ported to work on both platforms.
 *
 * @author ebo2022
 * @since 1.0
 */
public class BorealibChestBlockEntity extends ChestBlockEntity {

    protected BorealibChestBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public BorealibChestBlockEntity(BlockPos pos, BlockState state) {
        super(BorealibBlockEntityTypes.CHEST.get(), pos, state);
    }
}
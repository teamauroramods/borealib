package com.teamaurora.borealib.api.block.v1.entity;

import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

/**
 * A hanging sign block entity used for Borealib's hanging sign blocks.
 *
 * @author ebo2022
 * @since 1.0
 */
public class BorealibHangingSignBlockEntity extends HangingSignBlockEntity {

	public static final Set<Block> VALID_BLOCKS = new HashSet<>();

	public BorealibHangingSignBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return BorealibBlockEntityTypes.HANGING_SIGN.get();
	}
}
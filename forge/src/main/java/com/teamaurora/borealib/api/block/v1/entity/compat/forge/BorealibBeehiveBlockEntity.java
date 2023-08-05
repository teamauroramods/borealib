package com.teamaurora.borealib.api.block.v1.entity.compat.forge;

import com.teamaurora.borealib.core.registry.forge.BorealibForgeBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class BorealibBeehiveBlockEntity extends BeehiveBlockEntity {

	public BorealibBeehiveBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Nonnull
	@Override
	public BlockEntityType<?> getType() {
		return BorealibForgeBlockEntityTypes.BEEHIVE.get();
	}

}
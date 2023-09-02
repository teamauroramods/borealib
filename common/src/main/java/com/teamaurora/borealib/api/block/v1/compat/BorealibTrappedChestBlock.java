package com.teamaurora.borealib.api.block.v1.compat;

import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibTrappedChestBlockEntity;
import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A trapped chest block used in Borealib wood sets. Based on Blueprint's chest system and ported to work on both platforms.
 *
 * @author ebo2022
 * @since 1.0
 */
public class BorealibTrappedChestBlock extends ChestBlock implements ExtendedChestBlock {
    private final ResourceLocation type;

    public BorealibTrappedChestBlock(ResourceLocation type, Properties properties) {
        super(properties, BorealibBlockEntityTypes.TRAPPED_CHEST::get);
        this.type = type;
    }

    @Override
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return Mth.clamp(ChestBlockEntity.getOpenCount(blockGetter, blockPos), 0, 15);
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return direction == Direction.UP ? blockState.getSignal(blockGetter, blockPos, direction) : 0;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BorealibTrappedChestBlockEntity(blockPos, blockState);
    }

    @Override
    public ResourceLocation chestType() {
        return this.type;
    }
}

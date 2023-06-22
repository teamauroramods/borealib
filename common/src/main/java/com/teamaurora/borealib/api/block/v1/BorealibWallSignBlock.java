package com.teamaurora.borealib.api.block.v1;

import com.teamaurora.borealib.api.block.v1.entity.BorealibSignBlockEntity;
import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;

public class BorealibWallSignBlock extends WallSignBlock {

    public BorealibWallSignBlock(Properties properties, WoodType woodType) {
        super(properties, woodType);
        BorealibSignBlockEntity.VALID_BLOCKS.add(this);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BorealibBlockEntityTypes.SIGN.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BorealibBlockEntityTypes.SIGN.get(), SignBlockEntity::tick);
    }
}

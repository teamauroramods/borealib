package com.teamaurora.borealib.api.block.v1.compat;

import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibChestBlockEntity;
import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A chest block used in Borealib wood sets. Based on Blueprint's chest system and ported to work on both platforms.
 *
 * @author ebo2022
 * @since 1.0
 */
public class BorealibChestBlock extends ChestBlock implements ExtendedChestBlock {
    private final ResourceLocation type;

    public BorealibChestBlock(ResourceLocation type, Properties properties) {
        super(properties, BorealibBlockEntityTypes.CHEST::get);
        this.type = type;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BorealibChestBlockEntity(blockPos, blockState);
    }

    @Override
    public ResourceLocation chestType() {
        return this.type;
    }
}

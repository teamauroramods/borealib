package com.teamaurora.borealib.api.block.v1.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * bookshelf block for Quark, Woodworks, and Carpenter
 */
public class BookshelfBlock extends Block {

    public BookshelfBlock(Properties properties) {
        super(properties);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return 1.0F;
    }
}
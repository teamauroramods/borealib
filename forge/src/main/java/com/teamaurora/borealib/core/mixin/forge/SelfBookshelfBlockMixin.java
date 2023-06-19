package com.teamaurora.borealib.core.mixin.forge;

import com.teamaurora.borealib.api.block.v1.compat.BookshelfBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BookshelfBlock.class)
public abstract class SelfBookshelfBlockMixin extends Block {

    public SelfBookshelfBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return 1.0F;
    }
}

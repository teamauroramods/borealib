package com.teamaurora.borealib.api.event.world.v1;

import com.teamaurora.borealib.api.base.v1.event.Event;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public final class WorldEvents {

    public static final Event<Bonemeal> BONEMEAL = Event.createResult(Bonemeal.class);
    public static final Event<TreeGrowing> TREE_GROWING = Event.createResult(TreeGrowing.class);

    private WorldEvents() {
    }

    /**
     * Fired when a block is bonemealed.
     *
     * @author ebo2022
     * @since 2.0.0
     */
    @FunctionalInterface
    public interface Bonemeal {

        /**
         * Called when the specified block is about to be bonemealed.
         *
         * @param level   The level
         * @param pos     The position of the block being bonemealed
         * @param state   The block being bonemealed
         * @param stack   The bonemeal ItemStack in the player's hand
         * @return The result for this event. {@link InteractionResult#PASS} continues onto the next listener, while any others will override vanilla behavior
         */
        InteractionResult bonemeal(Level level, BlockPos pos, BlockState state, ItemStack stack);
    }

    /**
     * Fired when a sapling grows into a tree.
     *
     * @author ebo2022
     * @since 2.0.0
     */
    @FunctionalInterface
    public interface TreeGrowing {

        /**
         * Called when a {@link net.minecraft.world.level.block.SaplingBlock} is successfully grown.
         *
         * @param level The level the sapling is in
         * @param rand  An instance of {@link RandomSource} for use in code
         * @param pos   The origin position of the sapling
         * @return The result for this event. {@link InteractionResult#PASS} continues onto the next listener, while any others will override vanilla behavior
         */
        InteractionResult interaction(LevelAccessor level, RandomSource rand, BlockPos pos);
    }
}
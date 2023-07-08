package com.teamaurora.borealib.api.content_registries.v1;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * @author ebo2022
 * @since 1.0
 */
public interface FlammabilityRegistry {

    /**
     * Registers flammability for the specified blocks.
     *
     * @param encouragement The probability of the blocks lighting on fire. Used in <code>(encouragement + 40 + level.getDifficulty().getId() * 7) / (fireAge + 30)</code>
     * @param flammability  The probability of the blocks burning up on the fire tick. 0 is 0% and 300 is 100%
     * @param blocks        The blocks to light on fire
     */
    static void register(int encouragement, int flammability, Block... blocks) {
        register(Blocks.FIRE, encouragement, flammability, blocks);
    }

    /**
     * Registers flammability for the specified fire block.
     *
     * @param fireBlock     The block to use as fire. If unsure, use {@link #register(int, int, Block...)} instead
     * @param encouragement The probability of the blocks lighting on fire. Used in <code>(encouragement + 40 + level.getDifficulty().getId() * 7) / (fireAge + 30)</code>
     * @param flammability  The probability of the blocks burning up on the fire tick. 0 is 0% and 300 is 100%
     * @param blocks        The blocks to light on fire
     */
    static void register(Block fireBlock, int encouragement, int flammability, Block... blocks) {

    }
}
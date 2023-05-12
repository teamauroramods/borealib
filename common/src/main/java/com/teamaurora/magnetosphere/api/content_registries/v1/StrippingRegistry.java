package com.teamaurora.magnetosphere.api.content_registries.v1;

import com.teamaurora.magnetosphere.impl.content_registries.StrippingRegistryImpl;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface StrippingRegistry {

    /**
     * Register stripping behavior for the given blocks.
     *
     * @param from The block to be stripped
     * @param to   The stripped variant of the block
     */
    static void register(Block from, Block to) {
        StrippingRegistryImpl.register(from, to);
    }

    /**
     * Gets a stripped version of the given {@link BlockState} if it exists.
     *
     * @param from The blockstate to get the stripped variant for
     * @return The stripped version of the blockstate if it exists, otherwise {@code null}
     */
    @Nullable
    static BlockState getStrippedState(BlockState from) {
        return StrippingRegistryImpl.getStrippedState(from);
    }



    /**
     * Gives blocks finer control as to how block states transfer over to stripped variants.
     * <p>If this class isn't implemented then behavior will be automatically computed.
     *
     * @since 1.0.0
     */
    interface ConfiguredBlock {

        /**
         * Copies the properties from the specified state into this state.
         *
         * @param state The state to copy values from
         * @return This state with all copied properties from the provided state
         */
        BlockState copyStrippedPropertiesFrom(BlockState state);
    }
}
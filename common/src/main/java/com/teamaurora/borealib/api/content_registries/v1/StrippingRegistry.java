package com.teamaurora.borealib.api.content_registries.v1;

import com.teamaurora.borealib.impl.content_registries.StrippingRegistryImpl;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @author ebo2022
 * @since 1.0
 */
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
     * Gets a stripped version of the given {@link BlockState} if it was added by the registry.
     *
     * @param from The blockstate to get the stripped variant for
     * @return The stripped version of the blockstate if it exists, otherwise {@code null}
     */
    @Nullable
    static BlockState get(BlockState from) {
        return StrippingRegistryImpl.get(from);
    }
}
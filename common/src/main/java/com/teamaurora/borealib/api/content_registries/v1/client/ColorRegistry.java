package com.teamaurora.borealib.api.content_registries.v1.client;

import com.teamaurora.borealib.impl.content_registries.client.ColorRegistryImpl;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface ColorRegistry {

    /**
     * Registers colors for the given items.
     *
     * @param itemColor The item color function to get the colors
     * @param items     The items to add color to
     */
    @SafeVarargs
    static void register(ItemColor itemColor, Supplier<? extends ItemLike>... items) {
        ColorRegistryImpl.register(itemColor, items);
    }

    /**
     * Registers colors for the given items.
     *
     * @param blockColor The item color function to get the colors
     * @param blocks     The items to add color to
     */
    @SafeVarargs
    static void register(BlockColor blockColor, Supplier<? extends Block>... blocks) {
        ColorRegistryImpl.register(blockColor, blocks);
    }
}

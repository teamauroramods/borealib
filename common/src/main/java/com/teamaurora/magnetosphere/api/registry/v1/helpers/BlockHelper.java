package com.teamaurora.magnetosphere.api.registry.v1.helpers;

import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import com.teamaurora.magnetosphere.api.registry.v1.StandardRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public interface BlockHelper {

    /**
     * Registers a block with a simple item.
     *
     * @param id         The id of the block
     * @param block      The block to register
     * @param properties The properties of the item to register
     * @param <R>        The type of block being registered
     * @return The registered block
     */
    static <R extends Block> RegistryReference<R> registerWithItem(ResourceLocation id, Supplier<R> block, Item.Properties properties) {
        return registerWithItem(id, block, object -> new BlockItem(object, properties));
    }

    /**
     * Registers a block with an item.
     *
     * @param id          The id of the block
     * @param block       The block to register
     * @param itemFactory The factory to create a new item from the registered block
     * @param <R>         The type of block being registered
     * @return The registered block
     */
    static  <R extends Block> RegistryReference<R> registerWithItem(ResourceLocation id, Supplier<R> block, Function<R, Item> itemFactory) {
        RegistryReference<R> register = StandardRegistries.BLOCKS.register(id, block);
        StandardRegistries.ITEMS.register(id, () -> itemFactory.apply(register.get()));
        return register;
    }
}

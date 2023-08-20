package com.teamaurora.borealib.api.content_registries.v1;

import com.teamaurora.borealib.impl.content_registries.FuelRegistryImpl;
import net.minecraft.world.level.ItemLike;

/**
 * Used to register furnace fuel times for items.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface FuelRegistry {

    /**
     * Registers a burn time for the specified item.
     *
     * @param itemLike The item to add burn times for
     * @param burnTime The burn time to use
     */
    static void register(ItemLike itemLike, int burnTime) {
        FuelRegistryImpl.register(itemLike, burnTime);
    }
}

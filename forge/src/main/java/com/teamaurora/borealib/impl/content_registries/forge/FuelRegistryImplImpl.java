package com.teamaurora.borealib.impl.content_registries.forge;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
public class FuelRegistryImplImpl {

    private static final Map<Item, Integer> BURN_TIMES = new ConcurrentHashMap<>();

    public static void register(ItemLike itemLike, int burnTicks) {
        BURN_TIMES.put(itemLike.asItem(), burnTicks);
    }

    public static boolean hasBurnTime(Item item) {
        return BURN_TIMES.containsKey(item);
    }

    public static int getBurnTime(Item item) {
        return BURN_TIMES.getOrDefault(item, -1);
    }
}

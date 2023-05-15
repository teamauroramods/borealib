package com.teamaurora.magnetosphere.api.content_registries.v1;

import com.teamaurora.magnetosphere.impl.content_registries.client.render.DispenserBehaviorRegistryImpl;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.BiPredicate;

public interface DispenserBehaviorRegistry {

    static void register(ItemLike item, DispenseItemBehavior behavior) {
        DispenserBehaviorRegistryImpl.register(item, behavior);
    }

    static void register(ItemLike item, BiPredicate<BlockSource, ItemStack> condition, DispenseItemBehavior behavior) {
        DispenserBehaviorRegistryImpl.register(item, condition, behavior);
    }
}

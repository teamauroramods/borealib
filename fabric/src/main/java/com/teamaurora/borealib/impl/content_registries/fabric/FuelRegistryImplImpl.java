package com.teamaurora.borealib.impl.content_registries.fabric;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FuelRegistryImplImpl {

    public static void register(ItemLike itemLike, int burnTime) {
        FuelRegistry.INSTANCE.add(itemLike, burnTime);
    }
}

package com.teamaurora.borealib.impl.content_registries.fabric;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FlammabilityRegistryImplImpl {

    public static void register(Block fireBlock, int encouragement, int flammability, Block... blocks) {
        FlammableBlockRegistry registry = FlammableBlockRegistry.getInstance(fireBlock);
        for (Block block : blocks) {
            registry.add(block, encouragement, flammability);
        }
    }
}

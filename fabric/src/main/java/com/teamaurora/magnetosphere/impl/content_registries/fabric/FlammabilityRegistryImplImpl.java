package com.teamaurora.magnetosphere.impl.content_registries.fabric;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

@ApiStatus.Internal
public class FlammabilityRegistryImplImpl {
    public static void register(Block fireBlock, int encouragement, int flammability, Block... blocks) {
        FlammableBlockRegistry registry = FlammableBlockRegistry.getInstance(fireBlock);
        Arrays.asList(blocks).forEach(block -> registry.add(block, encouragement, flammability));
    }
}

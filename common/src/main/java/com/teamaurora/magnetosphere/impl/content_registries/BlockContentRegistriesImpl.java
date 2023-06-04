package com.teamaurora.magnetosphere.impl.content_registries;

import com.google.common.collect.ImmutableMap;
import com.teamaurora.magnetosphere.api.content_registries.v1.FlammableBlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

@ApiStatus.Internal
public class BlockContentRegistriesImpl {

    public static Map<Block, FlammableBlockEntry> INITIAL_FLAMMABLE_BLOCKS;

    public static void init() {
        ImmutableMap.Builder<Block, FlammableBlockEntry> builder = ImmutableMap.builder();
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.burnOdds.keySet().forEach(block ->
                builder.put(block, new FlammableBlockEntry(fireBlock.igniteOdds.getInt(block), fireBlock.burnOdds.getInt(block)))
        );
        INITIAL_FLAMMABLE_BLOCKS = builder.build();
    }
}

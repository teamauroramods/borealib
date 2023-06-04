package com.teamaurora.magnetosphere.impl.content_registries;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamaurora.magnetosphere.api.content_registries.v1.BlockContentRegistries;
import com.teamaurora.magnetosphere.api.content_registries.v1.ContentRegistry;
import com.teamaurora.magnetosphere.api.content_registries.v1.FlammableBlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

@ApiStatus.Internal
public class BlockContentRegistriesImpl {

    public static Map<Block, FlammableBlockEntry> INITIAL_FLAMMABLE_BLOCKS;

    public static void reloadFlammableBlocks(ContentRegistry<Block, FlammableBlockEntry> registry) {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.igniteOdds.clear();
        fireBlock.burnOdds.clear();
        fireBlock.igniteOdds.putAll(Maps.transformValues(BlockContentRegistriesImpl.INITIAL_FLAMMABLE_BLOCKS, FlammableBlockEntry::encouragement));
        fireBlock.burnOdds.putAll(Maps.transformValues(BlockContentRegistriesImpl.INITIAL_FLAMMABLE_BLOCKS, FlammableBlockEntry::flammability));
        registry.forEach((block, data) -> {
            fireBlock.igniteOdds.put(block, data.encouragement());
            fireBlock.burnOdds.put(block, data.flammability());
        });
    }

    public static void init() {
        ImmutableMap.Builder<Block, FlammableBlockEntry> builder = ImmutableMap.builder();
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.burnOdds.keySet().forEach(block ->
                builder.put(block, new FlammableBlockEntry(fireBlock.igniteOdds.getInt(block), fireBlock.burnOdds.getInt(block)))
        );
        INITIAL_FLAMMABLE_BLOCKS = builder.build();
    }
}

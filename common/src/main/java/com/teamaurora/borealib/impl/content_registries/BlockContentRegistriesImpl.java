package com.teamaurora.borealib.impl.content_registries;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.content_registries.v1.ContentRegistry;
import com.teamaurora.borealib.api.content_registries.v1.FlammableBlockEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.Item;
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
        registry.fullEntrySet().forEach((entry) -> {
            fireBlock.igniteOdds.put(entry.object(), entry.value().encouragement());
            fireBlock.burnOdds.put(entry.object(), entry.value().flammability());
        });
    }

    @ExpectPlatform
    public static void reloadBurnTimesOnFabric(ContentRegistry<Item, Integer> registry) {
        Platform.expect();
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

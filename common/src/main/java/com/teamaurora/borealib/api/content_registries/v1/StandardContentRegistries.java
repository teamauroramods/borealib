package com.teamaurora.borealib.api.content_registries.v1;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.base.v1.util.CodecHelper;
import com.teamaurora.borealib.api.base.v1.platform.PlatformHooks;
import com.teamaurora.borealib.api.registry.v1.RegistryView;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.impl.content_registries.BlockContentRegistriesImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.ApiStatus;

/**
 * Built-in content registries included with Borealib.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface StandardContentRegistries {

    /**
     * Manages interactions when an axe is used on any block with the <code>axis</code> block state property.
     */
    ContentRegistry<Block, Block> STRIPPING = ContentRegistries.create(Borealib.location("stripping"), RegistryView.BLOCK, CodecHelper.blockWithProperty(BlockStateProperties.AXIS));

    /**
     * Controls Flammability behavior.
     * <p>Encouragement represents how likely a block is to ignite, with 0 being 0% and 300 being 100%.
     * <p>Flammability represents how likely an ignited block is to spread fire to other blocks.
     *
     * @deprecated Use {@link PlatformHooks#getFlammability(BlockState, BlockGetter, BlockPos, Direction)} and {@link PlatformHooks#getFireSpreadSpeed(BlockState, BlockGetter, BlockPos, Direction)}
     */
    @Deprecated
    ContentRegistry<Block, FlammableBlockEntry> FLAMMABILITY = ContentRegistries.create(Borealib.location("flammability"), RegistryView.BLOCK, FlammableBlockEntry.CODEC, BlockContentRegistriesImpl::reloadFlammableBlocks);

    /**
     * Controls how an item can be used as fuel in a furnace.
     */
    ContentRegistry<Item, Integer> ITEM_BURN_TIMES = ContentRegistries.create(Borealib.location("burn_times"), RegistryView.ITEM, Codec.intRange(0, 32767));

    @ApiStatus.Internal
    static void init() {}
}

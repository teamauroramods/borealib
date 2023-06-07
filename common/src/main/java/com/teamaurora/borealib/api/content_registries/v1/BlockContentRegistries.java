package com.teamaurora.borealib.api.content_registries.v1;

import com.teamaurora.borealib.api.base.v1.util.CodecHelper;
import com.teamaurora.borealib.api.registry.v1.RegistryView;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.impl.content_registries.BlockContentRegistriesImpl;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public interface BlockContentRegistries {

    ContentRegistry<Block, Block> STRIPPING = ContentRegistries.create(Borealib.location("stripping"), RegistryView.BLOCK, CodecHelper.blockWithProperty(BlockStateProperties.AXIS));
    ContentRegistry<Block, FlammableBlockEntry> FLAMMABILITY = ContentRegistries.create(Borealib.location("flammability"), RegistryView.BLOCK, FlammableBlockEntry.CODEC, BlockContentRegistriesImpl::reloadFlammableBlocks);

    static void init() {}
}

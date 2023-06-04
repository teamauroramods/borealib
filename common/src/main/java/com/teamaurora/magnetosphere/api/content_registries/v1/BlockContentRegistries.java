package com.teamaurora.magnetosphere.api.content_registries.v1;

import com.teamaurora.magnetosphere.api.base.v1.util.CodecHelper;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import com.teamaurora.magnetosphere.impl.content_registries.BlockContentRegistriesImpl;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public interface BlockContentRegistries {

    ContentRegistry<Block, Block> STRIPPING = ContentRegistries.create(Magnetosphere.location("stripping"), RegistryView.BLOCK, CodecHelper.blockWithProperty(BlockStateProperties.AXIS));
    ContentRegistry<Block, FlammableBlockEntry> FLAMMABILITY = ContentRegistries.create(Magnetosphere.location("flammability"), RegistryView.BLOCK, FlammableBlockEntry.CODEC, BlockContentRegistriesImpl::reloadFlammableBlocks);

    static void init() {}
}

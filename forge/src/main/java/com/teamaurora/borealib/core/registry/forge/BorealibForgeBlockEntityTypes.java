package com.teamaurora.borealib.core.registry.forge;

import com.teamaurora.borealib.api.block.v1.compat.forge.BorealibBeehiveBlock;
import com.teamaurora.borealib.api.block.v1.entity.compat.forge.BorealibBeehiveBlockEntity;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import net.minecraft.Util;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BorealibForgeBlockEntityTypes {

    public static final RegistryReference<BlockEntityType<BorealibBeehiveBlockEntity>> BEEHIVE = BorealibBlockEntityTypes.BLOCK_ENTITIES.registerTyped("beehive", BorealibBeehiveBlockEntity::new, BorealibBeehiveBlock.class);

    public static void init() {}
}

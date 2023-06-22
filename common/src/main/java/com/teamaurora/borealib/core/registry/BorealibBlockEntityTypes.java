package com.teamaurora.borealib.core.registry;

import com.teamaurora.borealib.api.block.v1.compat.BorealibChestBlock;
import com.teamaurora.borealib.api.block.v1.compat.BorealibTrappedChestBlock;
import com.teamaurora.borealib.api.block.v1.entity.BorealibHangingSignBlockEntity;
import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibChestBlockEntity;
import com.teamaurora.borealib.api.block.v1.entity.BorealibSignBlockEntity;
import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibTrappedChestBlockEntity;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.extended.DeferredBlockEntityRegister;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.ApiStatus;

public final class BorealibBlockEntityTypes {

    @ApiStatus.Internal
    public static final DeferredBlockEntityRegister BLOCK_ENTITIES = DeferredBlockEntityRegister.create(Borealib.MOD_ID);

    // Blocks are populated later automatically
    public static final RegistryReference<BlockEntityType<BorealibSignBlockEntity>> SIGN = BLOCK_ENTITIES.registerDynamic("sign", BorealibSignBlockEntity::new, () -> BorealibSignBlockEntity.VALID_BLOCKS);
    public static final RegistryReference<BlockEntityType<BorealibHangingSignBlockEntity>> HANGING_SIGN = BLOCK_ENTITIES.registerDynamic("hanging_sign", BorealibHangingSignBlockEntity::new, () -> BorealibHangingSignBlockEntity.VALID_BLOCKS);
    public static final RegistryReference<BlockEntityType<BorealibChestBlockEntity>> CHEST = BLOCK_ENTITIES.registerDynamic("chest", BorealibChestBlockEntity::new, BorealibChestBlock.class);
    public static final RegistryReference<BlockEntityType<BorealibTrappedChestBlockEntity>> TRAPPED_CHEST = BLOCK_ENTITIES.registerDynamic("trapped_chest", BorealibTrappedChestBlockEntity::new, BorealibTrappedChestBlock.class);
}
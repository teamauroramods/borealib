package com.teamaurora.borealib.core.registry;

import com.teamaurora.borealib.api.block.v1.compat.BorealibChestBlock;
import com.teamaurora.borealib.api.block.v1.compat.BorealibTrappedChestBlock;
import com.teamaurora.borealib.api.block.v1.entity.BorealibHangingSignBlockEntity;
import com.teamaurora.borealib.api.block.v1.entity.BorealibSignBlockEntity;
import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibChestBlockEntity;
import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibTrappedChestBlockEntity;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.ApiStatus;

public final class BorealibBlockEntityTypes {

    @ApiStatus.Internal
    public static final RegistryWrapper.BlockEntityProvider BLOCK_ENTITIES = RegistryWrapper.blockEntityProvider(Borealib.MOD_ID);

    public static final RegistryReference<BlockEntityType<BorealibSignBlockEntity>> SIGN = BLOCK_ENTITIES.registerLazy("sign", BorealibSignBlockEntity::new, () -> BorealibSignBlockEntity.VALID_BLOCKS);
    public static final RegistryReference<BlockEntityType<BorealibHangingSignBlockEntity>> HANGING_SIGN = BLOCK_ENTITIES.registerLazy("hanging_sign", BorealibHangingSignBlockEntity::new, () -> BorealibHangingSignBlockEntity.VALID_BLOCKS);
    public static final RegistryReference<BlockEntityType<BorealibChestBlockEntity>> CHEST = BLOCK_ENTITIES.registerTyped("chest", BorealibChestBlockEntity::new, BorealibChestBlock.class);
    public static final RegistryReference<BlockEntityType<BorealibTrappedChestBlockEntity>> TRAPPED_CHEST = BLOCK_ENTITIES.registerTyped("trapped_chest", BorealibTrappedChestBlockEntity::new, BorealibTrappedChestBlock.class);
}
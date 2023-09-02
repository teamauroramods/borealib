package com.teamaurora.borealib.core;

import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibChestBlockEntity;
import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibTrappedChestBlockEntity;
import com.teamaurora.borealib.api.content_registries.v1.client.render.BlockEntityRendererRegistry;
import com.teamaurora.borealib.api.content_registries.v1.client.render.EntityRendererRegistry;
import com.teamaurora.borealib.api.item.v1.BEWLRBlockItem;
import com.teamaurora.borealib.core.client.render.block.entity.BorealibChestRenderer;
import com.teamaurora.borealib.core.client.render.block.entity.ChestBlockEntityWithoutLevelRenderer;
import com.teamaurora.borealib.core.client.render.entity.BorealibBoatRenderer;
import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import com.teamaurora.borealib.core.registry.BorealibEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BorealibClient {

    public static void init() {
        EntityRendererRegistry.register(BorealibEntityTypes.CHEST_BOAT, ctx -> new BorealibBoatRenderer<>(ctx, true));
        EntityRendererRegistry.register(BorealibEntityTypes.BOAT, ctx -> new BorealibBoatRenderer<>(ctx, false));
        BlockEntityRendererRegistry.register(BorealibBlockEntityTypes.CHEST, BorealibChestRenderer::new);
        BlockEntityRendererRegistry.register(BorealibBlockEntityTypes.TRAPPED_CHEST, BorealibChestRenderer::new);
        BlockEntityRendererRegistry.register(BorealibBlockEntityTypes.SIGN, SignRenderer::new);
        BlockEntityRendererRegistry.register(BorealibBlockEntityTypes.HANGING_SIGN, HangingSignRenderer::new);
    }

    @Environment(EnvType.CLIENT)
    public static BEWLRBlockItem.LazyBEWLR chestBEWLR(boolean trapped) {
        return trapped ? new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> new ChestBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new BorealibTrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState())))
                : new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> new ChestBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new BorealibChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState())));
    }

}

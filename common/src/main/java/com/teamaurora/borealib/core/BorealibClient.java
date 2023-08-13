package com.teamaurora.borealib.core;

import com.teamaurora.borealib.api.content_registries.v1.client.render.BlockEntityRendererRegistry;
import com.teamaurora.borealib.api.content_registries.v1.client.render.EntityRendererRegistry;
import com.teamaurora.borealib.core.client.render.block.entity.BorealibChestRenderer;
import com.teamaurora.borealib.core.client.render.block.entity.BorealibSignRenderer;
import com.teamaurora.borealib.core.client.render.entity.CustomBoatRenderer;
import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import com.teamaurora.borealib.core.registry.BorealibEntityTypes;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BorealibClient {

    public static void init() {
        EntityRendererRegistry.register(BorealibEntityTypes.CHEST_BOAT, ctx -> new CustomBoatRenderer<>(ctx, true));
        EntityRendererRegistry.register(BorealibEntityTypes.BOAT, ctx -> new CustomBoatRenderer<>(ctx, false));
        BlockEntityRendererRegistry.register(BorealibBlockEntityTypes.CHEST, BorealibChestRenderer::new);
        BlockEntityRendererRegistry.register(BorealibBlockEntityTypes.TRAPPED_CHEST, BorealibChestRenderer::new);
        BlockEntityRendererRegistry.register(BorealibBlockEntityTypes.SIGN, BorealibSignRenderer::new);
    }
}

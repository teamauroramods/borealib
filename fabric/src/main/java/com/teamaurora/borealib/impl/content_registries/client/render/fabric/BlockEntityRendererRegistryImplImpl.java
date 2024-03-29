package com.teamaurora.borealib.impl.content_registries.client.render.fabric;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockEntityRendererRegistryImplImpl {

    public static <T extends BlockEntity> void register(Supplier<? extends BlockEntityType<? extends T>> type, BlockEntityRendererProvider<T> factory) {
        BlockEntityRenderers.register(type.get(), factory);
    }
}

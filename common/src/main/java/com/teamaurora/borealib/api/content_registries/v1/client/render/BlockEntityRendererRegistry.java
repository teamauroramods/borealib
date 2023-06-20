package com.teamaurora.borealib.api.content_registries.v1.client.render;

import com.teamaurora.borealib.impl.content_registries.client.BlockEntityRendererRegistryImpl;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * Used to register renderers for block entities.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface BlockEntityRendererRegistry {

    /**
     * Registers a renderer factory for the specified block entity.
     *
     * @param type    The block entity to add rendering for
     * @param factory The factory to generate a new renderer
     * @param <T>     The block entity type
     */
    static <T extends BlockEntity> void register(Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<T> factory) {
        BlockEntityRendererRegistryImpl.register(type, factory);
    }
}

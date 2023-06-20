package com.teamaurora.borealib.api.content_registries.v1.client.render;

import com.teamaurora.borealib.impl.content_registries.client.render.RenderTypeRegistryImpl;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

/**
 * A helper class for registering block and fluid render types.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface RenderTypeRegistry {

    /**
     * Registers the specified render type for the given blocks.
     *
     * @param renderType The render type to add
     * @param blocks     The blocks to receive the render type
     */
    static void register(RenderType renderType, Block... blocks) {
        RenderTypeRegistryImpl.register(renderType, blocks);
    }

    /**
     * Registers the specified render type for the given fluids.
     *
     * @param renderType The render type to add
     * @param fluids     The fluids to receive the render type
     */
    static void register(RenderType renderType, Fluid... fluids) {
        RenderTypeRegistryImpl.register(renderType, fluids);
    }
}

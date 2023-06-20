package com.teamaurora.borealib.impl.content_registries.client.render;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class RenderTypeRegistryImpl {

    @ExpectPlatform
    public static void register(RenderType type, Block... blocks) {
        Platform.expect();
    }

    @ExpectPlatform
    public static void register(RenderType type, Fluid... fluids) {
        Platform.expect();
    }
}

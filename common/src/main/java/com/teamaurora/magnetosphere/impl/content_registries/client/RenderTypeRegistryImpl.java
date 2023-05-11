package com.teamaurora.magnetosphere.impl.content_registries.client;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class RenderTypeRegistryImpl {

    @ExpectPlatform
    public static void register(RenderType type, Block... blocks) {
        Platform.expect();
    }
}

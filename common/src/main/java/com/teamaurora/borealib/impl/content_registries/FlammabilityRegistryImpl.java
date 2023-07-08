package com.teamaurora.borealib.impl.content_registries;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FlammabilityRegistryImpl {

    @ExpectPlatform
    public static void register(Block fireBlock, int encouragement, int flammability, Block... blocks) {
        Platform.expect();
    }
}
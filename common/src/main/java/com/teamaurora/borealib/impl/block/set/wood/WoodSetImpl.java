package com.teamaurora.borealib.impl.block.set.wood;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class WoodSetImpl {

    @ExpectPlatform
    private static void includeCompatWoodVariants(WoodSet set) {
        Platform.expect();
    }
}

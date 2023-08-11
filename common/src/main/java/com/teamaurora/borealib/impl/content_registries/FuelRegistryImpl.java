package com.teamaurora.borealib.impl.content_registries;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FuelRegistryImpl {

    @ExpectPlatform
    public static void register(ItemLike itemLike, int burnTime) {
        Platform.expect();
    }
}

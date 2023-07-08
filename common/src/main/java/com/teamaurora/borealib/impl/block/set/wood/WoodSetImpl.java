package com.teamaurora.borealib.impl.block.set.wood;

import com.google.common.collect.ImmutableList;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.borealib.api.block.v1.set.variant.ItemVariant;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class WoodSetImpl {

    @ExpectPlatform
    public static void addPlatformBlockVariants(ImmutableList.Builder<BlockVariant<WoodSet>> builder) {
        Platform.expect();
    }

    @ExpectPlatform
    public static void addExtraPlatformBlockVariants(ImmutableList.Builder<BlockVariant<WoodSet>> builder) {
        Platform.expect();
    }

    @ExpectPlatform
    public static void addPlatformItemVariants(ImmutableList.Builder<ItemVariant<WoodSet>> builder) {
        Platform.expect();
    }
}

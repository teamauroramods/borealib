package com.teamaurora.borealib.impl.block.set.wood.forge;

import com.google.common.collect.ImmutableList;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.block.v1.compat.forge.ForgeCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.borealib.api.block.v1.set.variant.ItemVariant;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class WoodSetImplImpl {

    public static void addPlatformBlockVariants(ImmutableList.Builder<BlockVariant<WoodSet>> builder) {
        builder.add(ForgeCompatBlockVariants.STRIPPED_POST).add(ForgeCompatBlockVariants.POST).add(ForgeCompatBlockVariants.VERTICAL_PLANKS);
    }

    public static void addPlatformItemVariants(ImmutableList.Builder<ItemVariant<WoodSet>> builder) {

    }

    public static void addExtraPlatformBlockVariants(ImmutableList.Builder<BlockVariant<WoodSet>> builder) {
        builder.add(ForgeCompatBlockVariants.HEDGE);
    }
}

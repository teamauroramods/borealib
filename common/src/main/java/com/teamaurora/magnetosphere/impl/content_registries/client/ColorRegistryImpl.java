package com.teamaurora.magnetosphere.impl.content_registries.client;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Internal
public class ColorRegistryImpl {

    @SafeVarargs
    @ExpectPlatform
    public static void register(ItemColor itemColor, Supplier<? extends ItemLike>... items) {
        Platform.expect();
    }

    @SafeVarargs
    @ExpectPlatform
    public static void register(BlockColor blockColor, Supplier<? extends Block>... blocks) {
        Platform.expect();
    }
}

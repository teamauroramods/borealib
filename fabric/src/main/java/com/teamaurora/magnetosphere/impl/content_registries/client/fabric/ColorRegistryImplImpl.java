package com.teamaurora.magnetosphere.impl.content_registries.client.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Internal
public class ColorRegistryImplImpl {
    @SafeVarargs
    public static void register(ItemColor itemColor, Supplier<? extends ItemLike>... items) {
        for (Supplier<? extends ItemLike> item : items) {
            ColorProviderRegistry.ITEM.register(itemColor, item.get());
        }
    }

    @SafeVarargs
    public static void register(BlockColor blockColor, Supplier<? extends Block>... blocks) {
        for (Supplier<? extends Block> block : blocks) {
            ColorProviderRegistry.BLOCK.register(blockColor, block.get());
        }
    }
}

package com.teamaurora.borealib.api.entity.v1;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Holds the data for a custom modded boat type.
 *
 * @param texture             The regular boat texture
 * @param chestVariantTexture The chest boat texture
 * @param planks              The planks to use for drops when the game rule is enabled
 * @see com.teamaurora.borealib.api.item.v1.CustomBoatItem
 * @author ebo2022
 * @since 1.0
 */
public record CustomBoatType(ResourceLocation texture, ResourceLocation chestVariantTexture, Supplier<Block> planks) {
}
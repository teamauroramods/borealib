package com.teamaurora.borealib.api.entity.v1;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public record CustomBoatType(ResourceLocation texture, ResourceLocation chestVariantTexture, Supplier<Block> planks) {
}
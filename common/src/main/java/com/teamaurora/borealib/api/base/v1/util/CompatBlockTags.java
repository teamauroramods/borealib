package com.teamaurora.borealib.api.base.v1.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * Block tags for mods that Team Aurora supports compatibility with.
 *
 * @author ebo2022
 * @since 1.0
 */
public class CompatBlockTags {

	public static final TagKey<Block> LEAF_PILES = tag(Mods.WOODWORKS, "leaf_piles");
	public static final TagKey<Block> CARPENTER_CHESTS = tag(Mods.CARPENTER, "chests");
	public static final TagKey<Block> CARPENTER_TRAPPED_CHESTS = tag(Mods.CARPENTER, "trapped_chests");
	public static final TagKey<Block> CARPENTER_BOOKSHELVES = tag(Mods.CARPENTER, "bookshelves");
	public static final TagKey<Block> HEDGES = tag(Mods.QUARK, "hedges");
	public static final TagKey<Block> LADDERS = tag(Mods.QUARK, "ladders");
	public static final TagKey<Block> VERTICAL_SLABS = tag(Mods.QUARK, "vertical_slabs");
	public static final TagKey<Block> WOODEN_VERTICAL_SLABS = tag(Mods.QUARK, "wooden_vertical_slabs");
	public static final TagKey<Block> HOLLOW_LOGS = tag(Mods.QUARK, "hollow_logs");
	public static final TagKey<Block> CABINETS = tag(Mods.FARMERS_DELIGHT, "cabinets");
	public static final TagKey<Block> CABINETS_WOODEN = tag(Mods.FARMERS_DELIGHT, "cabinets/wooden");

	private static TagKey<Block> tag(String namespace, String path) {
		return TagKey.create(Registries.BLOCK, new ResourceLocation(namespace, path));
	}
}
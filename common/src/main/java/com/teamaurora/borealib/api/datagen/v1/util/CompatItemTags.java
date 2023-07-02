package com.teamaurora.borealib.api.datagen.v1.util;

import com.teamaurora.borealib.api.base.v1.util.Mods;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Item tags for mods that Team Aurora supports compatibility with.
 *
 * @author ebo2022
 * @since 1.0
 */
public class CompatItemTags {

	public static final TagKey<Item> FURNACE_BOATS = tag(Mods.BOATLOAD, "furnace_boats");
	public static final TagKey<Item> LARGE_BOATS = tag(Mods.BOATLOAD, "large_boats");
	public static final TagKey<Item> BOATABLE_CHESTS = tag(Mods.QUARK, "boatable_chests");
	public static final TagKey<Item> REVERTABLE_CHESTS = tag(Mods.QUARK, "revertable_chests");
	public static final TagKey<Item> CARPENTER_CHESTS = tag(Mods.CARPENTER, "chests");
	public static final TagKey<Item> CARPENTER_TRAPPED_CHESTS = tag(Mods.CARPENTER, "trapped_chests");
	public static final TagKey<Item> CARPENTER_BOOKSHELVES = tag(Mods.CARPENTER, "bookshelves");
	public static final TagKey<Item> HEDGES = tag(Mods.QUARK, "hedges");
	public static final TagKey<Item> LADDERS = tag(Mods.QUARK, "ladders");
	public static final TagKey<Item> VERTICAL_SLABS = tag(Mods.QUARK, "vertical_slabs");
	public static final TagKey<Item> WOODEN_VERTICAL_SLABS = tag(Mods.QUARK, "wooden_vertical_slabs");

	private static TagKey<Item> tag(String namespace, String path) {
		return TagKey.create(Registries.ITEM, new ResourceLocation(namespace, path));
	}
}
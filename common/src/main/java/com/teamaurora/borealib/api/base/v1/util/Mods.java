package com.teamaurora.borealib.api.base.v1.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Locale;

public enum Mods {

    MINECRAFT,
    FORGE,
    FABRIC,
    FABRIC_TAGS("c"),

    AUTOREGLIB,
    QUARK,

    ABNORMALS_DELIGHT,
    ALLUREMENT,
    ATMOSPHERIC,
    AUTUMNITY,
    BAMBOO_BLOCKS,
    BERRY_GOOD,
    BLUEPRINT,
    BUZZIER_BEES,
    CAVERNS_AND_CHASMS,
    CLAYWORKS,
    ENDERGETIC_EXPANSION("endergetic"),
    ENVIRONMENTAL,
    BOATLOAD,
    NEAPOLITAN,
    NETHER_EXTENSION,
    PERSONALITY,
    SAVAGE_AND_RAVAGE("savageandravage"),
    UPGRADE_AQUATIC,
    WOODWORKS,

    // Eltrut & Co.
    ADDENDUM,
    DIFFERENTIATE,
    FLAMBOYANT,
    LEPTON,
    MORE_RESPAWN_ANCHORS("morerespawnanchors"),
    TOTALLY_WILD,

    // Moonflower
    CARPENTER,
    TOLERABLE_CREEPERS("tolerablecreepers"),
    POLLEN,
    VANITY,
    ANCHOR,
    ETCHED,
    LOCKSMITH,
    MANNEQUINS,

    // Other
    CREATE,
    CRUMBS,
    FARMERS_DELIGHT("farmersdelight"),
    INFERNAL_EXPANSION("infernalexp");

    private final String id;

    Mods() {
        this.id = this.name().toLowerCase(Locale.ROOT);
    }

    Mods(String id) {
        this.id = id;
    }


    public <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> resourceKey, String path) {
        return TagKey.create(resourceKey, new ResourceLocation(this.id, path));
    }

    public TagKey<Block> blockTag(String path) {
        return this.tag(Registries.BLOCK, path);
    }

    public TagKey<Item> itemTag(String path) {
        return this.tag(Registries.ITEM, path);
    }
}

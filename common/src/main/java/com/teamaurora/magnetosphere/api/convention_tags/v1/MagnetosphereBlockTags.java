package com.teamaurora.magnetosphere.api.convention_tags.v1;

import com.teamaurora.magnetosphere.api.content_registries.v1.TagRegistry;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wraps Forge block tags.
 * <p>These tags can be used to access and generate data for platform-specific counterparts.
 */
public final class MagnetosphereBlockTags {

    private MagnetosphereBlockTags() {
    }

    private static final List<TagKey<Block>> ALL_TAGS = new ArrayList<>();
    public static final TagKey<Block> BARRELS = tag("barrels");
    public static final TagKey<Block> BARRELS_WOODEN = tag("barrels/wooden");
    public static final TagKey<Block> BOOKSHELVES = tag("bookshelves");
    public static final TagKey<Block> CHESTS = tag("chests");
    public static final TagKey<Block> CHESTS_ENDER = tag("chests/ender");
    public static final TagKey<Block> CHESTS_TRAPPED = tag("chests/trapped");
    public static final TagKey<Block> CHESTS_WOODEN = tag("chests/wooden");
    public static final TagKey<Block> COBBLESTONE = tag("cobblestone");
    public static final TagKey<Block> COBBLESTONE_NORMAL = tag("cobblestone/normal");
    public static final TagKey<Block> COBBLESTONE_INFESTED = tag("cobblestone/infested");
    public static final TagKey<Block> COBBLESTONE_MOSSY = tag("cobblestone/mossy");
    public static final TagKey<Block> COBBLESTONE_DEEPSLATE = tag("cobblestone/deepslate");
    public static final TagKey<Block> END_STONES = tag("end_stones");
    public static final TagKey<Block> ENDERMAN_PLACE_ON_BLACKLIST = tag("enderman_place_on_blacklist");
    public static final TagKey<Block> FENCE_GATES = tag("fence_gates");
    public static final TagKey<Block> FENCE_GATES_WOODEN = tag("fence_gates/wooden");
    public static final TagKey<Block> FENCES = tag("fences");
    public static final TagKey<Block> FENCES_NETHER_BRICK = tag("fences/nether_brick");
    public static final TagKey<Block> FENCES_WOODEN = tag("fences/wooden");
    public static final TagKey<Block> GLASS = tag("glass");
    public static final TagKey<Block> GLASS_BLACK = tag("glass/black");
    public static final TagKey<Block> GLASS_BLUE = tag("glass/blue");
    public static final TagKey<Block> GLASS_BROWN = tag("glass/brown");
    public static final TagKey<Block> GLASS_COLORLESS = tag("glass/colorless");
    public static final TagKey<Block> GLASS_CYAN = tag("glass/cyan");
    public static final TagKey<Block> GLASS_GRAY = tag("glass/gray");
    public static final TagKey<Block> GLASS_GREEN = tag("glass/green");
    public static final TagKey<Block> GLASS_LIGHT_BLUE = tag("glass/light_blue");
    public static final TagKey<Block> GLASS_LIGHT_GRAY = tag("glass/light_gray");
    public static final TagKey<Block> GLASS_LIME = tag("glass/lime");
    public static final TagKey<Block> GLASS_MAGENTA = tag("glass/magenta");
    public static final TagKey<Block> GLASS_ORANGE = tag("glass/orange");
    public static final TagKey<Block> GLASS_PINK = tag("glass/pink");
    public static final TagKey<Block> GLASS_PURPLE = tag("glass/purple");
    public static final TagKey<Block> GLASS_RED = tag("glass/red");
    public static final TagKey<Block> GLASS_SILICA = tag("glass/silica");
    public static final TagKey<Block> GLASS_TINTED = tag("glass/tinted");
    public static final TagKey<Block> GLASS_WHITE = tag("glass/white");
    public static final TagKey<Block> GLASS_YELLOW = tag("glass/yellow");
    public static final TagKey<Block> GLASS_PANES = tag("glass_panes");
    public static final TagKey<Block> GLASS_PANES_BLACK = tag("glass_panes/black");
    public static final TagKey<Block> GLASS_PANES_BLUE = tag("glass_panes/blue");
    public static final TagKey<Block> GLASS_PANES_BROWN = tag("glass_panes/brown");
    public static final TagKey<Block> GLASS_PANES_COLORLESS = tag("glass_panes/colorless");
    public static final TagKey<Block> GLASS_PANES_CYAN = tag("glass_panes/cyan");
    public static final TagKey<Block> GLASS_PANES_GRAY = tag("glass_panes/gray");
    public static final TagKey<Block> GLASS_PANES_GREEN = tag("glass_panes/green");
    public static final TagKey<Block> GLASS_PANES_LIGHT_BLUE = tag("glass_panes/light_blue");
    public static final TagKey<Block> GLASS_PANES_LIGHT_GRAY = tag("glass_panes/light_gray");
    public static final TagKey<Block> GLASS_PANES_LIME = tag("glass_panes/lime");
    public static final TagKey<Block> GLASS_PANES_MAGENTA = tag("glass_panes/magenta");
    public static final TagKey<Block> GLASS_PANES_ORANGE = tag("glass_panes/orange");
    public static final TagKey<Block> GLASS_PANES_PINK = tag("glass_panes/pink");
    public static final TagKey<Block> GLASS_PANES_PURPLE = tag("glass_panes/purple");
    public static final TagKey<Block> GLASS_PANES_RED = tag("glass_panes/red");
    public static final TagKey<Block> GLASS_PANES_WHITE = tag("glass_panes/white");
    public static final TagKey<Block> GLASS_PANES_YELLOW = tag("glass_panes/yellow");
    public static final TagKey<Block> GRAVEL = tag("gravel");
    public static final TagKey<Block> NETHERRACK = tag("netherrack");
    public static final TagKey<Block> OBSIDIAN = tag("obsidian");
    public static final TagKey<Block> ORE_BEARING_GROUND_DEEPSLATE = tag("ore_bearing_ground/deepslate");
    public static final TagKey<Block> ORE_BEARING_GROUND_NETHERRACK = tag("ore_bearing_ground/netherrack");
    public static final TagKey<Block> ORE_BEARING_GROUND_STONE = tag("ore_bearing_ground/stone");
    public static final TagKey<Block> ORE_RATES_DENSE = tag("ore_rates/dense");
    public static final TagKey<Block> ORE_RATES_SINGULAR = tag("ore_rates/singular");
    public static final TagKey<Block> ORE_RATES_SPARSE = tag("ore_rates/sparse");
    public static final TagKey<Block> ORES = tag("ores");
    public static final TagKey<Block> ORES_COAL = tag("ores/coal");
    public static final TagKey<Block> ORES_COPPER = tag("ores/copper");
    public static final TagKey<Block> ORES_DIAMOND = tag("ores/diamond");
    public static final TagKey<Block> ORES_EMERALD = tag("ores/emerald");
    public static final TagKey<Block> ORES_GOLD = tag("ores/gold");
    public static final TagKey<Block> ORES_IRON = tag("ores/iron");
    public static final TagKey<Block> ORES_LAPIS = tag("ores/lapis");
    public static final TagKey<Block> ORES_NETHERITE_SCRAP = tag("ores/netherite_scrap");
    public static final TagKey<Block> ORES_QUARTZ = tag("ores/quartz");
    public static final TagKey<Block> ORES_REDSTONE = tag("ores/redstone");
    public static final TagKey<Block> ORES_IN_GROUND_DEEPSLATE = tag("ores_in_ground/deepslate");
    public static final TagKey<Block> ORES_IN_GROUND_NETHERRACK = tag("ores_in_ground/netherrack");
    public static final TagKey<Block> ORES_IN_GROUND_STONE = tag("ores_in_ground/stone");
    public static final TagKey<Block> SAND = tag("sand");
    public static final TagKey<Block> SAND_COLORLESS = tag("sand/colorless");
    public static final TagKey<Block> SAND_RED = tag("sand/red");
    public static final TagKey<Block> SANDSTONE = tag("sandstone");
    public static final TagKey<Block> STAINED_GLASS = tag("stained_glass");
    public static final TagKey<Block> STAINED_GLASS_PANES = tag("stained_glass_panes");
    public static final TagKey<Block> STONE = tag("stone");
    public static final TagKey<Block> STORAGE_BLOCKS = tag("storage_blocks");
    public static final TagKey<Block> STORAGE_BLOCKS_AMETHYST = tag("storage_blocks/amethyst");
    public static final TagKey<Block> STORAGE_BLOCKS_COAL = tag("storage_blocks/coal");
    public static final TagKey<Block> STORAGE_BLOCKS_COPPER = tag("storage_blocks/copper");
    public static final TagKey<Block> STORAGE_BLOCKS_DIAMOND = tag("storage_blocks/diamond");
    public static final TagKey<Block> STORAGE_BLOCKS_EMERALD = tag("storage_blocks/emerald");
    public static final TagKey<Block> STORAGE_BLOCKS_GOLD = tag("storage_blocks/gold");
    public static final TagKey<Block> STORAGE_BLOCKS_IRON = tag("storage_blocks/iron");
    public static final TagKey<Block> STORAGE_BLOCKS_LAPIS = tag("storage_blocks/lapis");
    public static final TagKey<Block> STORAGE_BLOCKS_NETHERITE = tag("storage_blocks/netherite");
    public static final TagKey<Block> STORAGE_BLOCKS_QUARTZ = tag("storage_blocks/quartz");
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_COPPER = tag("storage_blocks/raw_copper");
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_GOLD = tag("storage_blocks/raw_gold");
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_IRON = tag("storage_blocks/raw_iron");
    public static final TagKey<Block> STORAGE_BLOCKS_REDSTONE = tag("storage_blocks/redstone");


    private static TagKey<Block> tag(String path) {
        TagKey<Block> tagKey = TagRegistry.bindBlock(Magnetosphere.location(path));
        ALL_TAGS.add(tagKey);
        return tagKey;
    }

    public static List<TagKey<Block>> getAllTags() {
        return Collections.unmodifiableList(ALL_TAGS);
    }
}
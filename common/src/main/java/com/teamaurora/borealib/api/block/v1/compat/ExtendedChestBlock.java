package com.teamaurora.borealib.api.block.v1.compat;

import net.minecraft.resources.ResourceLocation;

/**
 * A shared interface that allows Borealib chest blocks to be identified by their base name. Based on Blueprint's chest system and ported to work on both platforms.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface ExtendedChestBlock {

    /**
     * @return The base name of the chest block
     */
    ResourceLocation chestType();
}

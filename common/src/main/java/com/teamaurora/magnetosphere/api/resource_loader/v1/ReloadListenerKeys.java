package com.teamaurora.magnetosphere.api.resource_loader.v1;

import net.minecraft.resources.ResourceLocation;

/**
 * This class contains default keys for various Minecraft reload listeners.
 *
 * @see NamedReloadListener
 */
public interface ReloadListenerKeys {
    // client
    ResourceLocation SOUNDS = new ResourceLocation("minecraft:sounds");
    ResourceLocation FONTS = new ResourceLocation("minecraft:fonts");
    ResourceLocation MODELS = new ResourceLocation("minecraft:models");
    ResourceLocation LANGUAGES = new ResourceLocation("minecraft:languages");
    ResourceLocation TEXTURES = new ResourceLocation("minecraft:textures");

    // server
    ResourceLocation TAGS = new ResourceLocation("minecraft:tags");
    ResourceLocation RECIPES = new ResourceLocation("minecraft:recipes");
    ResourceLocation ADVANCEMENTS = new ResourceLocation("minecraft:advancements");
    ResourceLocation FUNCTIONS = new ResourceLocation("minecraft:functions");
    ResourceLocation LOOT_TABLES = new ResourceLocation("minecraft:loot_tables");
}

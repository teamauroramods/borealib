package com.teamaurora.borealib.core.registry;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifier;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifierAction;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeSelector;
import com.teamaurora.borealib.api.entity.v1.BorealibBoatType;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.api.registry.v1.SimpleRegistry;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class BorealibRegistries {
    public static final SimpleRegistry<BorealibBoatType> BOAT_TYPES = RegistryWrapper.createSimple(Borealib.location("boat_type"));
    public static final SimpleRegistry<Codec<? extends BiomeSelector>> BIOME_SELECTOR_TYPES = RegistryWrapper.createSimple(Borealib.location("biome_selector_type"));
    public static final SimpleRegistry<Codec<? extends BiomeModifierAction>> BIOME_MODIFIER_ACTION_TYPES = RegistryWrapper.createSimple(Borealib.location("biome_modifier_action_type"));
    public static final ResourceKey<? extends Registry<BiomeModifier>> BIOME_MODIFIERS = RegistryWrapper.dynamicRegistry(Borealib.location("biome_modifier"), BiomeModifier.DIRECT_CODEC);

}

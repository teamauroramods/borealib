package com.teamaurora.borealib.core.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifier;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifierAction;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeSelector;
import com.teamaurora.borealib.api.block.v1.compat.ChestVariant;
import com.teamaurora.borealib.api.entity.v1.CustomBoatType;
import com.teamaurora.borealib.api.registry.v1.DynamicRegistryHooks;
import com.teamaurora.borealib.api.registry.v1.SimpleCustomRegistry;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Internal
public final class BorealibRegistries {

    public static final SimpleCustomRegistry<CustomBoatType> BOAT_TYPES = SimpleCustomRegistry.create(Borealib.location("boat_type"));
    public static final SimpleCustomRegistry<Codec<? extends BiomeSelector>> BIOME_SELECTOR_TYPES = SimpleCustomRegistry.create(Borealib.location("biome_selector_type"));
    public static final SimpleCustomRegistry<Codec<? extends BiomeModifierAction>> BIOME_MODIFIER_ACTION_TYPES = SimpleCustomRegistry.create(Borealib.location("biome_modifier_action_type"));
    public static final SimpleCustomRegistry<Supplier<ChestVariant>> CHEST_VARIANTS = SimpleCustomRegistry.create(Borealib.location("chest_variants"));
    public static final ResourceKey<Registry<BiomeModifier>> BIOME_MODIFIERS = DynamicRegistryHooks.create(Borealib.location("biome_modifier"), BiomeModifier.DIRECT_CODEC);

    private BorealibRegistries() {
    }
}

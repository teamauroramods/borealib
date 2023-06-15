package com.teamaurora.borealib.core.mixin.fabric;

import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Biome.class)
public interface BiomeAccessor {

    @Accessor
    Biome.ClimateSettings getClimateSettings();
}

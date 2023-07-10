package com.teamaurora.borealib.core.mixin.forge;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.EnumMap;

@Mixin(ModContainer.class)
public interface ModContainerAccessor {

    @Accessor(value = "configs", remap = false)
    EnumMap<ModConfig.Type, ModConfig> getConfigs();
}

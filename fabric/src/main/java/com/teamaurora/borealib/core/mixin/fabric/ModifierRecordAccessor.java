package com.teamaurora.borealib.core.mixin.fabric;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.fabricmc.fabric.impl.biome.modification.BiomeModificationImpl$ModifierRecord")
public interface ModifierRecordAccessor {

    @Accessor("id")
    ResourceLocation getId();
}

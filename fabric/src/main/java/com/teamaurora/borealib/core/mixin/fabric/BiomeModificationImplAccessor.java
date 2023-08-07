package com.teamaurora.borealib.core.mixin.fabric;

import net.fabricmc.fabric.impl.biome.modification.BiomeModificationImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@Mixin(BiomeModificationImpl.class)
public interface BiomeModificationImplAccessor {

    @Accessor("modifiers")
    List<Object> getModifiers();

    @Accessor("modifiersUnsorted")
    void setModifiersUnsorted(boolean modifiersUnsorted);
}

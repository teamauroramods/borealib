package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(RegistryDataLoader.class)
public interface RegistryDataLoaderAccessor {

    @Mutable
    @Accessor("WORLDGEN_REGISTRIES")
    static void setWorldgenRegistries(List<RegistryDataLoader.RegistryData<?>> data) {
        Platform.expect();
    }
}

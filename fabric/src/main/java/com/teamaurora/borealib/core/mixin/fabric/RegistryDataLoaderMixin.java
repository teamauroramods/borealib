package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.impl.registry.fabric.RegistryWrapperImplImpl;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {

    // Mirror forge's directory style for Borealib registries
    @Inject(method = "registryDirPath", at = @At("HEAD"), cancellable = true)
    private static void registryDirPath(ResourceLocation key, CallbackInfoReturnable<String> cir) {
        if (RegistryWrapperImplImpl.shouldPrefix(key))
            cir.setReturnValue(key.getNamespace() + "/" + key.getPath());
    }
}
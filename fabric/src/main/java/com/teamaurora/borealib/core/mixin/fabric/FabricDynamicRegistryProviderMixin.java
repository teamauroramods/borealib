package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.impl.registry.fabric.RegistryWrapperImplImpl;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FabricDynamicRegistryProvider.class)
public class FabricDynamicRegistryProviderMixin {

    // Makes data generation for Borealib dynamic registries output the correct directories
    @Redirect(method = "writeRegistryEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;getPath()Ljava/lang/String;"))
    private String handleBorealibDynamicRegistries(ResourceLocation instance) {
        return RegistryWrapperImplImpl.prefix(instance);
    }
}

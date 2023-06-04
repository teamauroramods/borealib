package com.teamaurora.magnetosphere.core.mixin.client;

import com.teamaurora.magnetosphere.api.event.lifecycle.v1.ResourceLoaderEvents;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

    @Dynamic
    @Inject(
            method = "method_41851(Lnet/minecraft/server/packs/resources/CloseableResourceManager;Lnet/minecraft/server/ReloadableServerResources;Lnet/minecraft/core/LayeredRegistryAccess;Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen$DataPackReloadCookie;)Lnet/minecraft/client/gui/screens/worldselection/WorldCreationContext;",
            at = @At("HEAD")
    )
    private static void onEndDataPackLoadOnOpen(CloseableResourceManager resourceManager, ReloadableServerResources resources, LayeredRegistryAccess<?> layeredRegistryAccess, @Coerce Object c_mxqwwbun, CallbackInfoReturnable<WorldCreationContext> cir) {
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(null, resourceManager, null);
    }
}

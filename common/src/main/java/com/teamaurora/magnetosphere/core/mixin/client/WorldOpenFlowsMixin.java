package com.teamaurora.magnetosphere.core.mixin.client;

import com.teamaurora.magnetosphere.api.event.lifecycle.v1.ResourceLoaderEvents;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldOpenFlows.class)
public class WorldOpenFlowsMixin {


    @Inject(method = "loadWorldDataBlocking", at = @At("RETURN"))
    private <D, R> void onEndDataPackLoad(WorldLoader.PackConfig packConfig, WorldLoader.WorldDataSupplier<D> loadContextSupplier,
                                          WorldLoader.ResultFactory<D, R> resultFactory, CallbackInfoReturnable<R> cir) {
        if (cir.getReturnValue() instanceof WorldStem worldStem) {
            ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(null, worldStem.resourceManager(), null);
        }
    }

    @Dynamic
    @Inject(
            method = "method_45695(Lnet/minecraft/server/packs/resources/CloseableResourceManager;Lnet/minecraft/server/ReloadableServerResources;Lnet/minecraft/core/LayeredRegistryAccess;Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;)Lcom/mojang/datafixers/util/Pair;",
            at = @At("HEAD")
    )
    private static void onEndDataPackLoad(CloseableResourceManager resourceManager, ReloadableServerResources resources,
                                          LayeredRegistryAccess<?> layeredRegistryAccess, @Coerce Object c_tattaqxb,
                                          CallbackInfoReturnable<Pair<?, ?>> cir) {
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(null, resourceManager, null);
    }

    @ModifyArg(
            method = {"createFreshLevel", "doLoadLevel"},
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V"),
            index = 1
    )
    private Throwable onFailedDataPackLoad(Throwable throwable) {
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(null, null, throwable);
        return throwable; // noop
    }
}

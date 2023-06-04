package com.teamaurora.magnetosphere.core.mixin;

import com.teamaurora.magnetosphere.api.event.lifecycle.v1.ResourceLoaderEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow
    public abstract ResourceManager getResourceManager();

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void onReloadResourcesEnd(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        cir.getReturnValue().handleAsync((value, throwable) -> {
            ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload((MinecraftServer) (Object) this,
                    this.getResourceManager(), throwable);
            return value;
        }, (MinecraftServer) (Object) this);
    }
}

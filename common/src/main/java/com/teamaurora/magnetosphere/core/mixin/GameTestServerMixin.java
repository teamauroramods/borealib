package com.teamaurora.magnetosphere.core.mixin;

import com.teamaurora.magnetosphere.api.event.lifecycle.v1.ResourceLoaderEvents;
import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.server.WorldStem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameTestServer.class)
public class GameTestServerMixin {

    @ModifyVariable(method = "create", at = @At(value = "STORE"))
    private static WorldStem onSuccessfulReloadResources(WorldStem resources) {
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(null, resources.resourceManager(), null);
        return resources; // noop
    }

    @ModifyArg(method = "create", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V"), index = 1)
    private static Throwable onFailedReloadResources(Throwable exception) {
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(null, null, exception);
        return exception; // noop
    }
}

package com.teamaurora.magnetosphere.core.mixin;

import com.teamaurora.magnetosphere.api.event.lifecycle.v1.ResourceLoaderEvents;
import net.minecraft.server.Main;
import net.minecraft.server.WorldStem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Main.class)
public class MainMixin {

    @ModifyVariable(method = "main", at = @At(value = "STORE"), remap = false)
    private static WorldStem onSuccessfulReloadResources(WorldStem resources) {
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(null, resources.resourceManager(), null);
        return resources; // noop
    }

    @ModifyArg(
            method = "main",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V"
            ),
            index = 1,
            remap = false
    )
    private static Throwable onFailedReloadResources(Throwable exception) {
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(null, null, exception);
        return exception; // noop
    }
}

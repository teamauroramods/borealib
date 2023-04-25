package dev.tesseract.mixin.fabric.client;

import dev.tesseract.api.base.v1.event.events.lifecycle.ServerLifecycleEvents;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    @Inject(method = "initServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/server/IntegratedServer;loadLevel()V", shift = At.Shift.BEFORE), cancellable = true)
    public void preStart(CallbackInfoReturnable<Boolean> cir) {
        if (!ServerLifecycleEvents.PRE_STARTING.invoker().forServer((MinecraftServer) (Object) this))
            cir.setReturnValue(false);
    }

    @Inject(method = "initServer", at = @At("TAIL"), cancellable = true)
    public void starting(CallbackInfoReturnable<Boolean> cir) {
        if (!ServerLifecycleEvents.STARTING.invoker().forServer((MinecraftServer) (Object) this))
            cir.setReturnValue(false);
    }
}
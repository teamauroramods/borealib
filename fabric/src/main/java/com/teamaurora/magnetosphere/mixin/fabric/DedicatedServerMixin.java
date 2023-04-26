package com.teamaurora.magnetosphere.mixin.fabric;

import com.teamaurora.magnetosphere.api.base.v1.event.events.lifecycle.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {

    @Inject(method = "initServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/GameProfileCache;setUsesAuthentication(Z)V", shift = At.Shift.AFTER), cancellable = true)
    public void preStart(CallbackInfoReturnable<Boolean> cir) {
        System.out.println("dfshgkjfhgkjdfhkjghkfdjhgkjfhdgkjhfdkjghfkdjhgkfdhgkjhfdkjghdkfjhg");
        if (!ServerLifecycleEvents.PRE_STARTING.invoker().forServer((MinecraftServer) (Object) this))
            cir.setReturnValue(false);
    }

    @Inject(method = "initServer", at = @At("TAIL"), cancellable = true)
    public void starting(CallbackInfoReturnable<Boolean> cir) {
        if (!ServerLifecycleEvents.STARTING.invoker().forServer((MinecraftServer) (Object) this))
            cir.setReturnValue(false);
    }
}
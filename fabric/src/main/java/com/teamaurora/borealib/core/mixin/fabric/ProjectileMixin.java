package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.api.event.entity.v1.ProjectileImpactEvent;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin {

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    public void onHit(HitResult hitResult, CallbackInfo ci) {
        if (!ProjectileImpactEvent.EVENT.invoker().onProjectileImpact((Projectile) (Object) this, hitResult))
            ci.cancel();
    }
}
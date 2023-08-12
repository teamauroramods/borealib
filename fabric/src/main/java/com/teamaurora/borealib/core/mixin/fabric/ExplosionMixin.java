package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.api.event.world.v1.ExplosionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Set;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow
    @Final
    private Level level;

    @Inject(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V", ordinal = 1, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    public void explode(CallbackInfo ci, Set<BlockPos> set, int k, float q,  int l, int r, int s, int t, int u, int v, List<Entity> list) {
        ExplosionEvents.DETONATE.invoker().detonate(this.level, (Explosion) (Object) this, list);
    }
}
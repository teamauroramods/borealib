package com.teamaurora.borealib.core.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.teamaurora.borealib.api.base.v1.util.MutableBoolean;
import com.teamaurora.borealib.api.base.v1.util.MutableFloat;
import com.teamaurora.borealib.api.event.entity.v1.LivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private MutableFloat blockedDamage;

    @Unique
    private MutableBoolean loseDurability;

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getHealth()F", shift = At.Shift.BEFORE), ordinal = 0, argsOnly = true)
    private float modifyDamageAmount(float value, DamageSource damageSource) {
        MutableFloat mutableDamage = MutableFloat.of(value);
        boolean event = LivingEntityEvents.DAMAGE.invoker().livingDamage((LivingEntity) (Object) this, damageSource, mutableDamage);
        return event ? mutableDamage.getAsFloat() : 0.0F;
    }

    @ModifyVariable(method = "heal", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float modifyHealAmount(float value) {
        MutableFloat mutableRegen = MutableFloat.of(value);
        boolean event = LivingEntityEvents.HEAL.invoker().heal((LivingEntity) (Object) this, mutableRegen);
        return event ? mutableRegen.getAsFloat() : 0.0F;
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void die(DamageSource damageSource, CallbackInfo ci) {
        if (!LivingEntityEvents.DEATH.invoker().death((LivingEntity) (Object) this, damageSource))
            ci.cancel();
    }

    @ModifyVariable(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V", shift = At.Shift.AFTER), ordinal = 2)
    private float modifyDamage1(float value) {
        return this.blockedDamage.getAsFloat();
    }

    @ModifyVariable(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V", shift = At.Shift.AFTER), ordinal = 0, argsOnly = true)
    private float modifyDamage2(float value) {
        return value - this.blockedDamage.getAsFloat();
    }

    @ModifyExpressionValue(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isDamageSourceBlocked(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    private boolean addCancellationCheck(boolean original, DamageSource damageSource, float f) {
        this.blockedDamage = MutableFloat.of(f);
        this.loseDurability = MutableBoolean.of(true);
        return original && LivingEntityEvents.SHIELD_BLOCK.invoker().onShieldBlock((LivingEntity) (Object) this, damageSource, f, this.blockedDamage, this.loseDurability);
    }

    @WrapWithCondition(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V"))
    private boolean modifyDurability(LivingEntity instance, float value) {
        return this.loseDurability.getAsBoolean();
    }
}

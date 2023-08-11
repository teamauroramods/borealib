package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.api.event.entity.v1.player.PlayerEvents;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {

    @Inject(method = "playerTouch", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Player;takeXpDelay:I", ordinal = 1, shift = At.Shift.BEFORE), cancellable = true)
    private void playerTouch(Player player, CallbackInfo ci) {
        if (!PlayerEvents.EXP_PICKUP.invoker().expPickup(player, (ExperienceOrb) (Object) this))
            ci.cancel();
    }
}
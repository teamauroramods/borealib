package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.api.event.entity.v1.player.PlayerEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceResultSlot.class)
public class FurnaceResultSlotMixin {

    @Shadow
    @Final
    private Player player;

    @Inject(method = "checkTakeAchievements", at = @At("TAIL"))
    public void checkTakeAchievements(ItemStack stack, CallbackInfo ci) {
        PlayerEvents.ITEM_SMELTED.invoker().smelt(this.player, stack);
    }
}
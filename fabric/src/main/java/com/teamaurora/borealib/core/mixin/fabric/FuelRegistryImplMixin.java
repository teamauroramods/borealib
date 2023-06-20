package com.teamaurora.borealib.core.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.teamaurora.borealib.api.content_registries.v1.StandardContentRegistries;
import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

// Hacky workaround, but it's the only real way to get it to work with Fabric :p
// shouldn't cause performance problems as it only adds the burn times during query operations
@SuppressWarnings("UnstableApiUsage")
@Mixin(FuelRegistryImpl.class)
public class FuelRegistryImplMixin {

    @ModifyReturnValue(method = "getFuelTimes", at = @At("RETURN"))
    private Map<Item, Integer> getFuelTimes(Map<Item, Integer> original) {
        StandardContentRegistries.ITEM_BURN_TIMES.fullEntrySet().forEach(entry -> original.put(entry.object(), entry.value()));
        return original;
    }
}

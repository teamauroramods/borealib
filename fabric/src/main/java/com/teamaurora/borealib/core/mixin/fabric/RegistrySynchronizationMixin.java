package com.teamaurora.borealib.core.mixin.fabric;

import com.google.common.collect.ImmutableMap;
import com.teamaurora.borealib.impl.registry.fabric.RegistryWrapperImplImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(RegistrySynchronization.class)
public class RegistrySynchronizationMixin {

    @Inject(method = "method_45958", at = @At(value = "RETURN", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void addNetworkRegistries(CallbackInfoReturnable<Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>>> cir, ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> builder) {
        builder.putAll(RegistryWrapperImplImpl.getNetworkableDynamicRegistries());
    }
}
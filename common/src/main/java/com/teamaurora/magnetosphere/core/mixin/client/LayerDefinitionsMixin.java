package com.teamaurora.magnetosphere.core.mixin.client;

import com.google.common.collect.ImmutableMap;
import com.teamaurora.magnetosphere.core.client.render.entity.CustomBoatRenderer;
import com.teamaurora.magnetosphere.core.registry.MagnetosphereRegistries;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

// Boat types may not all exist at the time of Magnetosphere's clientInit hence why it's here
@Mixin(LayerDefinitions.class)
public class LayerDefinitionsMixin {

    @Inject(method = "createRoots", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/SignRenderer;createSignLayer()Lnet/minecraft/client/model/geom/builders/LayerDefinition;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void createRoots(CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir, ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder) {
        MagnetosphereRegistries.BOAT_TYPES.stream().forEach(type -> {
            builder.put(CustomBoatRenderer.createChestBoatModelName(type), ChestBoatModel.createBodyModel());
            builder.put(CustomBoatRenderer.createBoatModelName(type), BoatModel.createBodyModel());
        });
    }
}
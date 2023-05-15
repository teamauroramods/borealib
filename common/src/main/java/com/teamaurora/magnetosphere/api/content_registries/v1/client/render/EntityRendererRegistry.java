package com.teamaurora.magnetosphere.api.content_registries.v1.client.render;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.impl.content_registries.client.render.EntityRendererRegistryImpl;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public interface EntityRendererRegistry {

    static <T extends Entity> void register(Supplier<EntityType<T>> type, EntityRendererProvider<T> factory) {
        EntityRendererRegistryImpl.register(type, factory);
    }

    static void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
        EntityRendererRegistryImpl.registerLayerDefinition(layerLocation, supplier);
    }
}
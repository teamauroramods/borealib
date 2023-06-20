package com.teamaurora.borealib.api.content_registries.v1.client.render;

import com.teamaurora.borealib.impl.content_registries.client.render.EntityRendererRegistryImpl;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

/**
 * A helper class for registering renderers and model layer definitions for entities.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface EntityRendererRegistry {

    /**
     * Registers a renderer factory for the specified entity type.
     *
     * @param type    The entity type to add rendering for
     * @param factory The factory to create an entity renderer
     * @param <T>     The entity type
     */
    static <T extends Entity> void register(Supplier<EntityType<T>> type, EntityRendererProvider<T> factory) {
        EntityRendererRegistryImpl.register(type, factory);
    }

    /**
     * Registers a layer definition with the specified location.
     *
     * @param layerLocation The location of the layer definition
     * @param supplier      The layer definition to register
     */
    static void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
        EntityRendererRegistryImpl.registerLayerDefinition(layerLocation, supplier);
    }
}
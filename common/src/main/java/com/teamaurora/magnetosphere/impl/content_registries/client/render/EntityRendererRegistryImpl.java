package com.teamaurora.magnetosphere.impl.content_registries.client.render;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Internal
public class EntityRendererRegistryImpl {

    @ExpectPlatform
    public static <T extends Entity> void register(Supplier<EntityType<T>> type, EntityRendererProvider<T> factory) {
        Platform.expect();
    }

    @ExpectPlatform
    public static void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
        Platform.expect();
    }
}
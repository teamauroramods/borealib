package com.teamaurora.magnetosphere.core;

import com.teamaurora.magnetosphere.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.magnetosphere.api.content_registries.v1.client.render.EntityRendererRegistry;
import com.teamaurora.magnetosphere.core.client.render.entity.CustomBoatRenderer;
import com.teamaurora.magnetosphere.core.network.MagnetosphereMessages;
import com.teamaurora.magnetosphere.core.registry.MagnetosphereEntityTypes;
import com.teamaurora.magnetosphere.impl.convention_tags.ConventionTagSynchronizer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

@ApiStatus.Internal
public class Magnetosphere implements ModLoaderService {

    public static final String MOD_ID = "magnetosphere";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ModLoaderService findMod(String id) {
        return ServiceLoader.load(ModLoaderService.class)
                .stream()
                .filter(p -> p.get().id().equals(id))
                .findFirst()
                .map(ServiceLoader.Provider::get)
                .orElseThrow(() -> new IllegalStateException("Couldn't find mod service with the id" + id));
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @Override
    public void onClientInit() {
        EntityRendererRegistry.register(MagnetosphereEntityTypes.CHEST_BOAT, ctx -> new CustomBoatRenderer<>(ctx, true));
        EntityRendererRegistry.register(MagnetosphereEntityTypes.BOAT, ctx -> new CustomBoatRenderer<>(ctx, false));
    }

    @Override
    public String id() {
        return MOD_ID;
    }

    @Override
    public void onCommonInit() {
        ConventionTagSynchronizer.init();
    }

    @Override
    public void onCommonPostInit(ParallelDispatcher dispatcher) {
        MagnetosphereMessages.init();
    }
}

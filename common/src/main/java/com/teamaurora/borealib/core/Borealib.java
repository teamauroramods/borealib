package com.teamaurora.borealib.core;

import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.borealib.api.content_registries.v1.BlockContentRegistries;
import com.teamaurora.borealib.api.content_registries.v1.client.render.EntityRendererRegistry;
import com.teamaurora.borealib.core.client.render.entity.CustomBoatRenderer;
import com.teamaurora.borealib.core.network.BorealibMessages;
import com.teamaurora.borealib.core.registry.BorealibEntityTypes;
import com.teamaurora.borealib.impl.content_registries.ContentRegistriesImpl;
import com.teamaurora.borealib.impl.convention_tags.ConventionTagSynchronizer;
import com.teamaurora.magnetosphere.impl.content_registries.BlockContentRegistriesImpl;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

@ApiStatus.Internal
public class Borealib implements ModLoaderService {

    public static final String MOD_ID = "borealib";
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
        EntityRendererRegistry.register(BorealibEntityTypes.CHEST_BOAT, ctx -> new CustomBoatRenderer<>(ctx, true));
        EntityRendererRegistry.register(BorealibEntityTypes.BOAT, ctx -> new CustomBoatRenderer<>(ctx, false));
    }

    @Override
    public String id() {
        return MOD_ID;
    }

    @Override
    public void onCommonInit() {
        ConventionTagSynchronizer.init();
        BlockContentRegistries.init();
        BlockContentRegistriesImpl.init();
        ContentRegistriesImpl.init();
    }

    @Override
    public void onCommonPostInit(ParallelDispatcher dispatcher) {
        BorealibMessages.init();
    }
}

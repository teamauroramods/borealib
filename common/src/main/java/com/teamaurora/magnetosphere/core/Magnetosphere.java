package com.teamaurora.magnetosphere.core;

import com.google.common.collect.Maps;
import com.teamaurora.magnetosphere.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.magnetosphere.api.content_registries.v1.BlockContentRegistries;
import com.teamaurora.magnetosphere.api.content_registries.v1.ContentRegistries;
import com.teamaurora.magnetosphere.api.content_registries.v1.FlammableBlockEntry;
import com.teamaurora.magnetosphere.api.content_registries.v1.client.render.EntityRendererRegistry;
import com.teamaurora.magnetosphere.api.event.lifecycle.v1.ResourceLoaderEvents;
import com.teamaurora.magnetosphere.core.client.render.entity.CustomBoatRenderer;
import com.teamaurora.magnetosphere.core.network.MagnetosphereMessages;
import com.teamaurora.magnetosphere.core.registry.MagnetosphereEntityTypes;
import com.teamaurora.magnetosphere.impl.content_registries.BlockContentRegistriesImpl;
import com.teamaurora.magnetosphere.impl.content_registries.ContentRegistriesImpl;
import com.teamaurora.magnetosphere.impl.convention_tags.ConventionTagSynchronizer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
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
        BlockContentRegistries.init();
        BlockContentRegistriesImpl.init();
        ContentRegistriesImpl.init();
        // Fabric doesn't modify the odds maps, so it's ok to clear them
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, error) -> {
            FireBlock fireBlock = (FireBlock) Blocks.FIRE;
            fireBlock.igniteOdds.clear();
            fireBlock.burnOdds.clear();
            fireBlock.igniteOdds.putAll(Maps.transformValues(BlockContentRegistriesImpl.INITIAL_FLAMMABLE_BLOCKS, FlammableBlockEntry::encouragement));
            fireBlock.burnOdds.putAll(Maps.transformValues(BlockContentRegistriesImpl.INITIAL_FLAMMABLE_BLOCKS, FlammableBlockEntry::flammability));
            BlockContentRegistries.FLAMMABILITY.forEach((block, data) -> {
                fireBlock.igniteOdds.put(block, data.encouragement());
                fireBlock.burnOdds.put(block, data.flammability());
            });
        });
    }

    @Override
    public void onCommonPostInit(ParallelDispatcher dispatcher) {
        MagnetosphereMessages.init();
    }
}

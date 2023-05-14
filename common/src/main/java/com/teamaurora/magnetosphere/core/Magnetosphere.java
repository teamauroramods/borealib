package com.teamaurora.magnetosphere.core;

import com.teamaurora.magnetosphere.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.magnetosphere.api.config.v1.ConfigBuilder;
import com.teamaurora.magnetosphere.api.config.v1.ConfigRegistry;
import com.teamaurora.magnetosphere.api.config.v1.ConfigValue;
import com.teamaurora.magnetosphere.api.config.v1.ModConfig;
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
    public String id() {
        return MOD_ID;
    }

    @Override
    public void onCommonInit() {
    }

    @Override
    public void onCommonPostInit(ParallelDispatcher dispatcher) {
    }
}

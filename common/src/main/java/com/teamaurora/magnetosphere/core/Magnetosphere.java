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
    public static final EtchedConfig.Client CONFIG = ConfigRegistry.register(MOD_ID, ModConfig.Type.SERVER, EtchedConfig.Client::new);
    public static final EtchedConfig.Client CONFIG2 = ConfigRegistry.register(MOD_ID, ModConfig.Type.COMMON, EtchedConfig.Client::new);

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

    public static class EtchedConfig {

        public static class Client {

            public final ConfigValue<Boolean> showNotes;

            public Client(ConfigBuilder builder) {
                builder.push("Game Feel");
                this.showNotes = builder.comment("Displays note particles appear above jukeboxes while a record is playing.").define("Display Note Particles", true);
                builder.pop();
            }
        }
    }
}

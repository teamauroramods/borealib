package com.teamaurora.borealib.core;

import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.content_registries.v1.StandardContentRegistries;
import com.teamaurora.borealib.api.content_registries.v1.client.render.EntityRendererRegistry;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionRegistry;
import com.teamaurora.borealib.core.client.render.entity.CustomBoatRenderer;
import com.teamaurora.borealib.core.network.BorealibMessages;
import com.teamaurora.borealib.core.registry.BorealibEntityTypes;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeModifierActions;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeSelectors;
import com.teamaurora.borealib.impl.content_registries.BlockContentRegistriesImpl;
import com.teamaurora.borealib.impl.content_registries.ContentRegistriesImpl;
import com.teamaurora.borealib.impl.resource_condition.ConfigResourceCondition;
import com.teamaurora.borealib.impl.resource_condition.RegistryKeyExistsResourceCondition;
import com.teamaurora.borealib.impl.resource_condition.TestsEnabledResourceCondition;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

@ApiStatus.Internal
public class Borealib implements ModLoaderService {

    public static final String MOD_ID = "borealib";
    public static final boolean TESTS_ENABLED;
    public static final Logger LOGGER = LogManager.getLogger();

    static {
        TESTS_ENABLED = "true".equalsIgnoreCase(System.getProperty("borealib.enableTests"));
        if (TESTS_ENABLED)
            LogManager.getLogger().info("Borealib testing mode enabled!");
    }

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

        // Init deferred registers
        BorealibEntityTypes.ENTITIES.register();
        BuiltInBiomeSelectors.WRITER.register();
        BuiltInBiomeModifierActions.WRITER.register();
        WoodSet.BOAT_TYPE_WRITER.register();

        // Init built-in stuff
        StandardContentRegistries.init();
        BlockContentRegistriesImpl.init();
        ContentRegistriesImpl.init();
        ResourceConditionRegistry.register(ConfigResourceCondition.NAME, new ConfigResourceCondition());
        ResourceConditionRegistry.register(RegistryKeyExistsResourceCondition.NAME, new RegistryKeyExistsResourceCondition());
        ResourceConditionRegistry.register(TestsEnabledResourceCondition.NAME, new TestsEnabledResourceCondition());
    }

    @Override
    public void onCommonPostInit(ParallelDispatcher dispatcher) {
        BorealibMessages.init();
    }
}

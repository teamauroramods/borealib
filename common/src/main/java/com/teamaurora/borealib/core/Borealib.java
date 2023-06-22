package com.teamaurora.borealib.core;

import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.content_registries.v1.StandardContentRegistries;
import com.teamaurora.borealib.api.content_registries.v1.client.render.BlockEntityRendererRegistry;
import com.teamaurora.borealib.api.content_registries.v1.client.render.EntityRendererRegistry;
import com.teamaurora.borealib.api.resource_condition.v1.DelegateResourceCondition;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionRegistry;
import com.teamaurora.borealib.core.client.render.block.entity.BorealibChestRenderer;
import com.teamaurora.borealib.core.client.render.block.entity.BorealibSignRenderer;
import com.teamaurora.borealib.core.client.render.entity.CustomBoatRenderer;
import com.teamaurora.borealib.core.network.BorealibMessages;
import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import com.teamaurora.borealib.core.registry.BorealibEntityTypes;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeModifierActions;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeSelectors;
import com.teamaurora.borealib.impl.content_registries.BlockContentRegistriesImpl;
import com.teamaurora.borealib.impl.content_registries.ContentRegistriesImpl;
import com.teamaurora.borealib.impl.resource_condition.ConfigResourceCondition;
import com.teamaurora.borealib.impl.resource_condition.DefaultResourceConditionsImpl;
import com.teamaurora.borealib.impl.resource_condition.RegistryKeyExistsResourceCondition;
import com.teamaurora.borealib.impl.resource_condition.TestsEnabledResourceCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

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

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @Override
    public void onClientInit() {
        EntityRendererRegistry.register(BorealibEntityTypes.CHEST_BOAT, ctx -> new CustomBoatRenderer<>(ctx, true));
        EntityRendererRegistry.register(BorealibEntityTypes.BOAT, ctx -> new CustomBoatRenderer<>(ctx, false));
        BlockEntityRendererRegistry.register(BorealibBlockEntityTypes.CHEST, BorealibChestRenderer::new);
        BlockEntityRendererRegistry.register(BorealibBlockEntityTypes.TRAPPED_CHEST, BorealibChestRenderer::new);
        BlockEntityRendererRegistry.register(BorealibBlockEntityTypes.SIGN, BorealibSignRenderer::new);
    }

    @Override
    public String id() {
        return MOD_ID;
    }

    @Override
    public void onCommonInit() {

        // Init deferred registers
        BorealibEntityTypes.ENTITIES.register();
        BorealibBlockEntityTypes.BLOCK_ENTITIES.register();;
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
        ResourceConditionRegistry.register(DefaultResourceConditionsImpl.QUARK_FLAG, new DelegateResourceCondition(Borealib.location("quark_flag"), new ResourceLocation("quark:flag"), (original, mapped) -> {
            mapped.addProperty("flag", GsonHelper.getAsString(original, "flag"));
        }));
        ResourceConditionRegistry.register(DefaultResourceConditionsImpl.WOODWORKS_FLAG, new DelegateResourceCondition(Borealib.location("woodworks_flag"), new ResourceLocation("woodworks:config"), (original, mapped) -> {
            mapped.addProperty("value", GsonHelper.getAsString(original, "value"));
        }));
    }

    @Override
    public void onCommonPostInit(ParallelDispatcher dispatcher) {
        BorealibMessages.init();
    }
}

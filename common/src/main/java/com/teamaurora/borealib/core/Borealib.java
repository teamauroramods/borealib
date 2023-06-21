package com.teamaurora.borealib.core;

import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.borealib.api.block.v1.compat.BorealibChestBlock;
import com.teamaurora.borealib.api.block.v1.compat.ChestVariant;
import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibChestBlockEntity;
import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibSignBlockEntity;
import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibTrappedChestBlockEntity;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.content_registries.v1.StandardContentRegistries;
import com.teamaurora.borealib.api.content_registries.v1.client.render.BlockEntityRendererRegistry;
import com.teamaurora.borealib.api.content_registries.v1.client.render.EntityRendererRegistry;
import com.teamaurora.borealib.api.event.creativetabs.v1.CreativeTabEvents;
import com.teamaurora.borealib.api.item.v1.BEWLRBlockItem;
import com.teamaurora.borealib.api.registry.v1.DeferredRegister;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryView;
import com.teamaurora.borealib.api.registry.v1.extended.DeferredBlockRegister;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionRegistry;
import com.teamaurora.borealib.core.client.render.block.entity.BorealibChestRenderer;
import com.teamaurora.borealib.core.client.render.block.entity.BorealibSignRenderer;
import com.teamaurora.borealib.core.client.render.block.entity.ChestBlockEntityWithoutLevelRenderer;
import com.teamaurora.borealib.core.client.render.entity.CustomBoatRenderer;
import com.teamaurora.borealib.core.network.BorealibMessages;
import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import com.teamaurora.borealib.core.registry.BorealibEntityTypes;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeModifierActions;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeSelectors;
import com.teamaurora.borealib.impl.content_registries.BlockContentRegistriesImpl;
import com.teamaurora.borealib.impl.content_registries.ContentRegistriesImpl;
import com.teamaurora.borealib.impl.resource_condition.ConfigResourceCondition;
import com.teamaurora.borealib.impl.resource_condition.RegistryKeyExistsResourceCondition;
import com.teamaurora.borealib.impl.resource_condition.TestsEnabledResourceCondition;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
    }

    @Override
    public void onCommonPostInit(ParallelDispatcher dispatcher) {
        BorealibMessages.init();
    }
}

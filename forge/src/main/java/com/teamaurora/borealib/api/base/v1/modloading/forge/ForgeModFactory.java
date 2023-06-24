package com.teamaurora.borealib.api.base.v1.modloading.forge;

import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.borealib.api.base.v1.platform.ModContainer;
import com.teamaurora.borealib.api.base.v1.util.forge.ForgeHelper;
import com.teamaurora.borealib.api.datagen.v1.BorealibDataGenerator;
import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.impl.base.modloading.forge.ParallelDispatcherImpl;
import com.teamaurora.borealib.impl.base.platform.forge.ModContainerImpl;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

/**
 * Handles Forge {@link ModLoaderService} initialization.
 *
 * @author ebo2022
 * @since 1.0.0
 */
public final class ForgeModFactory {

    private ForgeModFactory() {
    }

    /**
     * Initialized the {@link ModLoaderService} with the given id.
     * <p>This should be called in the mod constructor.
     *
     * @param id The id of the mod to initialize; should be the same one as specified in your {@link ModLoaderService}
     */
    public static void loadMod(String id) {
        IEventBus bus = ForgeHelper.getEventBus(id);
        ModLoaderService s = ModLoaderService.byId(id);
        bus.<FMLCommonSetupEvent>addListener(e -> s.onCommonPostInit(new ParallelDispatcherImpl(e)));
        bus.<FMLClientSetupEvent>addListener(e -> s.onClientPostInit(new ParallelDispatcherImpl(e)));
        bus.<FMLDedicatedServerSetupEvent>addListener(e -> s.onServerPostInit(new ParallelDispatcherImpl(e)));
        bus.<GatherDataEvent>addListener(e -> {
            s.onDataInit(new BorealibDataGenerator() {
                ModContainer container = new ModContainerImpl(e.getModContainer());
                private BorealibPackOutput borealibOutput;

                @Override
                public boolean includeClient() {
                    return e.includeClient();
                }

                @Override
                public boolean includeServer() {
                    return e.includeServer();
                }

                @Override
                public boolean includeDev() {
                    return false;
                }

                @Override
                public boolean includeReports() {
                    return false;
                }

                @Override
                public <T extends DataProvider> T addProvider(boolean run, BorealibDataGenerator.Factory<T> factory) {
                    return e.getGenerator().<T>addProvider(run, output -> factory.create(this.getOrCreatePackOutput(output)));
                }

                @Override
                public <T extends DataProvider> T addProvider(boolean run, BorealibDataGenerator.RegistryDependentFactory<T> factory) {
                    return e.getGenerator().<T>addProvider(run, output -> factory.create(this.getOrCreatePackOutput(output), e.getLookupProvider()));
                }

                private BorealibPackOutput getOrCreatePackOutput(PackOutput vanilla) {
                    if (borealibOutput == null) borealibOutput = new BorealibPackOutput(container, vanilla);
                    return borealibOutput;
                }
            });
            RegistrySetBuilder builder = new RegistrySetBuilder();
            s.buildRegistries(builder);
            // only add a provider if the builder is actually being used
            if (!builder.getEntryKeys().isEmpty())
                e.getGenerator().addProvider(e.includeServer(), (DataProvider.Factory<DynamicRegistryHandler>) output -> new DynamicRegistryHandler(builder, id, output, e.getLookupProvider()));
        });
        s.onCommonInit();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> s::onClientInit);
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> s::onServerInit);
    }

    private static class DynamicRegistryHandler extends DatapackBuiltinEntriesProvider {

        private final String modId;

        private DynamicRegistryHandler(RegistrySetBuilder builder, String modId, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, builder, Set.of(modId));
            this.modId = modId;
        }

        @Override
        public @NotNull String getName() {
            return "Dynamic Registries: " + this.modId;
        }
    }
}

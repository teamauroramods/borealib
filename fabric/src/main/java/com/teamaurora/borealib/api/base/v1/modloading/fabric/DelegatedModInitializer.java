package com.teamaurora.borealib.api.base.v1.modloading.fabric;

import com.teamaurora.borealib.api.base.v1.platform.Environment;
import com.teamaurora.borealib.api.base.v1.platform.ModContainer;
import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.borealib.api.datagen.v1.BorealibDataGenerator;
import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.impl.base.platform.fabric.ModContainerImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Handles Fabric {@link ModLoaderService} initialization.
 * <p>In <code>fabric.mod.json</code>, this should be listed as a mod initializer entrypoint AND a datagen entrypoint if required.
 */
public interface DelegatedModInitializer extends ModInitializer, DataGeneratorEntrypoint {

    /**
     * @return The id of the mod to initialize; should be the same one as specified in your {@link ModLoaderService}
     */
    String id();

    @Override
    default void onInitialize() {
        ModLoaderService s = ModLoaderService.byId(this.id());
        ModLoaderService.ParallelDispatcher dispatcher = new ModLoaderService.ParallelDispatcher() {
            @Override
            public CompletableFuture<Void> enqueueWork(Runnable work) {
                work.run();
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
                return CompletableFuture.completedFuture(work.get());
            }
        };
        s.onCommonInit();
        s.onCommonPostInit(dispatcher);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            s.onClientInit();
            s.onClientPostInit(dispatcher);
        } else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            s.onServerInit();
            s.onServerPostInit(dispatcher);
        }
    }

    @Override
    default void onInitializeDataGenerator(FabricDataGenerator generator) {
        ModLoaderService.byId(this.id()).onDataInit(new BorealibDataGenerator() {
            ModContainer container = new ModContainerImpl(generator.getModContainer());
            FabricDataGenerator.Pack pack = generator.createPack();
            private BorealibPackOutput borealibOutput;

            @Override
            public boolean includeClient() {
                return true;
            }

            @Override
            public boolean includeServer() {
                return true;
            }

            @Override
            public boolean includeDev() {
                return true;
            }

            @Override
            public boolean includeReports() {
                return true;
            }

            @Override
            public <T extends DataProvider> T addProvider(boolean run, BorealibDataGenerator.Factory<T> factory) {
                return pack.addProvider((FabricDataGenerator.Pack.Factory<T>) output -> factory.create(this.getOrCreatePackOutput(output)));
            }

            @Override
            public <T extends DataProvider> T addProvider(boolean run, BorealibDataGenerator.RegistryDependentFactory<T> factory) {
                return pack.addProvider((output, registriesFuture) -> factory.create(this.getOrCreatePackOutput(output), registriesFuture));
            }

            private BorealibPackOutput getOrCreatePackOutput(FabricDataOutput fabric) {
                if (borealibOutput == null) borealibOutput = new BorealibPackOutput(new ModContainerImpl(fabric.getModContainer()), fabric);
                return borealibOutput;
            }
        });
    }

    @Override
    default void buildRegistry(RegistrySetBuilder builder) {
        ModLoaderService.byId(this.id()).buildRegistries(builder);
    }
}
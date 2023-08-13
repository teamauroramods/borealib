package com.teamaurora.borealib.api.base.v1.util.forge;

import com.teamaurora.borealib.api.base.v1.platform.ModContainer;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.base.v1.util.ParallelDispatcher;
import com.teamaurora.borealib.api.datagen.v1.BorealibDataGenerator;
import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.api.datagen.v1.RegistrySetWrapper;
import com.teamaurora.borealib.core.mixin.forge.ModContainerAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ForgeHelper {

    /**
     * Attempts to get the registered configs under a given mod id.
     *
     * @param modId The mod id to get configs for
     * @return The config type to config map if it exists
     */
    static Optional<EnumMap<ModConfig.Type, ModConfig>> getRawConfigData(String modId) {
        return ModList.get().getModContainerById(modId).map(container -> ((ModContainerAccessor) container).getConfigs());
    }

    /**
     * Gets the event bus for the specified mod id.
     *
     * @param modId The mod id
     * @return An event bus for the given mod id
     */
    static IEventBus getEventBus(String modId) {
        return ((FMLModContainer) ModList.get().getModContainerById(modId).orElseThrow(() -> new IllegalArgumentException("Invalid mod: " + modId))).getEventBus();
    }

    /**
     * Creates a {@link ParallelDispatcher} wrapping the given {@link ParallelDispatchEvent}.
     *
     * @param event The event to wrap
     * @return A corresponding {@link ParallelDispatcher}
     */
    static <T extends ParallelDispatchEvent> ParallelDispatcher createDispatcher(T event) {
        return new ParallelDispatcherImpl(event);
    }

    /**
     * Accepts dynamic registries to be generated for a {@link GatherDataEvent}.
     *
     * @param event    The event to add a provider to
     * @param consumer The consumer to add registries to be generated
     */
    static void buildRegistries(GatherDataEvent event, Consumer<RegistrySetWrapper> consumer) {
        RegistrySetBuilder builder = new RegistrySetBuilder();
        consumer.accept(new RegistrySetWrapper() {
            @Override
            public <T> RegistrySetWrapper add(ResourceKey<? extends Registry<T>> registry, RegistrySetBuilder.RegistryBootstrap<T> bootstrap) {
                builder.add(registry, bootstrap);
                return this;
            }
        });
        event.getGenerator().addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), builder, Set.of(event.getModContainer().getModId())));
    }

    /**
     * Creates a {@link BorealibDataGenerator} to wrap a forge event.
     *
     * @param e The event to wrap
     * @return A new {@link BorealibDataGenerator} wrapping the original eveny
     */
    static BorealibDataGenerator createGenerator(GatherDataEvent e) {
        return new BorealibDataGenerator() {
            ModContainer container = Platform.getMod(e.getModContainer().getModId()).orElseThrow();
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
        };
    }

    class ParallelDispatcherImpl implements ParallelDispatcher {

        private final ParallelDispatchEvent event;

        public ParallelDispatcherImpl(ParallelDispatchEvent event) {
            this.event = event;
        }

        @Override
        public CompletableFuture<Void> enqueueWork(Runnable work) {
            return this.event.enqueueWork(work);
        }

        @Override
        public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
            return this.event.enqueueWork(work);
        }
    }
}

package com.teamaurora.borealib.api.base.v1.util.fabric;

import com.teamaurora.borealib.api.base.v1.platform.ModContainer;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.datagen.v1.BorealibDataGenerator;
import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.api.datagen.v1.RegistrySetWrapper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public interface FabricHelper {

    /**
     * Accepts dynamic registries to be generated for a data generator entrypoint.
     *
     * @param builder  The builder to add registry elements to
     * @param consumer The consumer to add registries to be generated
     */
    static void buildRegistries(RegistrySetBuilder builder, Consumer<RegistrySetWrapper> consumer) {
        consumer.accept(new RegistrySetWrapper() {
            @Override
            public <T> RegistrySetWrapper add(ResourceKey<? extends Registry<T>> registry, RegistrySetBuilder.RegistryBootstrap<T> bootstrap) {
                builder.add(registry, bootstrap);
                return this;
            }
        });
    }

    /**
     * Adds a provider to generate the entries added by {@link #buildRegistries(RegistrySetBuilder, Consumer)}.
     *
     * @param pack     The pack to add the provider to
     * @param consumer The consumer to look for registries to be generated
     */
    static void generateRegistries(FabricDataGenerator.Pack pack, Consumer<RegistrySetWrapper> consumer) {
        Set<ResourceKey<? extends Registry<?>>> keys = new HashSet<>();
        consumer.accept(new RegistrySetWrapper() {
            @Override
            public <T> RegistrySetWrapper add(ResourceKey<? extends Registry<T>> registry, RegistrySetBuilder.RegistryBootstrap<T> bootstrap) {
                keys.add(registry);
                return this;
            }
        });
        pack.addProvider((output, registriesFuture) -> new FabricDynamicRegistryProvider(output, registriesFuture) {
            @Override
            protected void configure(HolderLookup.Provider registries, Entries entries) {
                keys.forEach(key -> entries.addAll(registries.lookupOrThrow(key)));
            }

            @Override
            public String getName() {
                return "Registries";
            }
        });
    }

    /**
     * Creates a {@link BorealibDataGenerator} to wrap a fabric version.
     *
     * @param generator The generator to wrap
     * @param pack      The pack to add providers to
     * @return A new {@link BorealibDataGenerator} wrapping the original generator
     */
    static BorealibDataGenerator createGenerator(FabricDataGenerator generator, FabricDataGenerator.Pack pack) {
        return new BorealibDataGenerator() {
            ModContainer container = Platform.getMod(generator.getModId()).orElseThrow();
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
                if (borealibOutput == null) borealibOutput = new BorealibPackOutput(container, fabric);
                return borealibOutput;
            }
        };
    }
}

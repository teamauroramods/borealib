package com.teamaurora.borealib.api.datagen.v1;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

/**
 * Wraps each platforms' methods of handling data generation.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface BorealibDataGenerator {

    /**
     * @return Whether to run client generators (only affects Forge)
     */
    boolean includeClient();

    /**
     * @return Whether to run server generators (only affects Forge)
     */
    boolean includeServer();

    /**
     * @return Whether to run dev environment generators (only affects Forge)
     */
    boolean includeDev();

    /**
     * @return Whether to run reports generators (only affects Forge)
     */
    boolean includeReports();

    /**
     * Adds a data provider to be generated.
     *
     * @param run     Whether to run the generator (use one of the include...() methods, only affects Forge)
     * @param factory The factory to generate the data provider
     * @param <T>     The data provider type
     * @return The registered data provider
     */
    <T extends DataProvider> T addProvider(boolean run, Factory<T> factory);

    /**
     * Adds a data provider to be generated.
     *
     * @param registryDependentFactory The factory to generate the data provider
     * @param <T>                      The data provider type
     * @return The registered data provider
     */
    <T extends DataProvider> T addProvider(boolean run, RegistryDependentFactory<T> registryDependentFactory);

    /**
     * Generates a data provider. This is typically the constructor reference if there is no complex behavior involved.
     *
     * @param <T> The data provider type
     * @since 1.0
     */
    @FunctionalInterface
    interface Factory<T extends DataProvider> {

        /**
         * Generates the data provider based on the current pack output.
         *
         * @param output The current pack output
         * @return The constructed data provider
         */
        T create(BorealibPackOutput output);
    }

    /**
     * Generates a data provider dependent on existing registries. This is typically the constructor reference if there is no complex behavior involved.
     *
     * @param <T> The data provider type
     * @since 1.0
     */
    @FunctionalInterface
    interface RegistryDependentFactory<T extends DataProvider> {

        /**
         * Generates the data provider based on the current pack output and registry lookup.
         *
         * @param output     The current pack output
         * @param registries A {@link CompletableFuture} for the existing registries
         * @return The constructed data provider
         */
        T create(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> registries);
    }
}

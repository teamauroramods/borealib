package com.teamaurora.borealib.api.base.v1.modloading;

import com.teamaurora.borealib.api.base.v1.platform.ModContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ModLoaderService {

    String id();

    default void onClientInit() {
    }

    default void onClientPostInit(ParallelDispatcher dispatcher) {
    }

    default void onCommonInit() {
    }

    default void onCommonPostInit(ParallelDispatcher dispatcher) {
    }

    default void onServerInit() {
    }

    default void onServerPostInit(ParallelDispatcher dispatcher) {
    }

    default void onDataInit(DataGeneratorContext context) {
    }

    default void buildRegistries(RegistrySetBuilder builder) {
    }


    /**
     * Wraps enqueue code for Forge's <code>ParallelDispatch</code>-based events. Some code may need to be enqueued to ensure proper thread safety.
     *
     * @since 1.0
     */
    interface ParallelDispatcher {

        /**
         * Queues work to happen later when it is safe to do so.
         * <p><i>NOTE: The returned future may execute on the current thread so it is not safe to call {@link CompletableFuture#join()} or {@link CompletableFuture#get()}</i>
         *
         * @param work The work to do
         * @return A future for when the work is done
         */
        CompletableFuture<Void> enqueueWork(Runnable work);

        /**
         * Queues work to happen later when it is safe to do so.
         * <p><i>NOTE: The returned future may execute on the current thread so it is not safe to call {@link CompletableFuture#join()} or {@link CompletableFuture#get()}</i>
         *
         * @param work The work to do
         * @return A future for when the work is done
         */
        <T> CompletableFuture<T> enqueueWork(Supplier<T> work);
    }


    /**
     * Used as context for initializing data generators.
     *
     * @since 1.0.0
     */
    interface DataGeneratorContext {

        <T extends DataProvider> T addProvider(DataProvider.Factory<T> factory);

        <T extends DataProvider> T addProvider(BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> registryDependentFactory);

        /**
         * @return The mod the generator is running for
         */
        ModContainer getContainer();
    }
}

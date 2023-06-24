package com.teamaurora.borealib.api.base.v1.modloading;

import com.teamaurora.borealib.api.base.v1.platform.Environment;
import com.teamaurora.borealib.api.base.v1.platform.ModContainer;
import com.teamaurora.borealib.api.datagen.v1.BorealibDataGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The primary class for initializing mods.
 *
 * <p>Dependent mods should have one class that implements this interface and registers it in <code>META-INF/services</code>.
 * <p>Fabric users: Use a <code>DelegatedModInitializer</code> and specify it as a main entrypoint (and a datagen one if necessary) in <code>fabric.mod.json</code>.
 * <p>Forge users: Run <code>ForgeModFactory.loadMod()</code> using the same id as this service in your constructor.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface ModLoaderService {

    /**
     * @return The id to identify the service by; this should be the same everywhere the service is initialized
     */
    String id();

    /**
     * Ran if the current environment is a client. Register client content here.
     */
    default void onClientInit() {
    }

    /**
     * Ran if the current environment is a client.
     * <p>On Fabric this runs in Borealib's mod entrypoint and on Forge wraps <code>FMLClientSetupEvent</code>. Register non-threadsafe server content here.
     * <p>Registry contents of Borealib-dependent mods should be guaranteed to be registered at this point.
     *
     * @param dispatcher The parallel dispatcher to use for deferring non-threadsafe code
     */
    default void onClientPostInit(ParallelDispatcher dispatcher) {
    }

    /**
     * Runs immediately upon mod construction in all environments. Register common content here.
     */
    default void onCommonInit() {
    }

    /**
     * On Fabric this runs in Borealib's mod entrypoint and on Forge wraps <code>FMLCommonSetupEvent</code>. Register non-threadsafe server content here.
     * <p>Registry contents of Borealib-dependent mods should be guaranteed to be registered at this point.
     *
     * @param dispatcher The parallel dispatcher to use for deferring non-threadsafe code
     */
    default void onCommonPostInit(ParallelDispatcher dispatcher) {
    }

    /**
     * Ran if the current environment is a server. Register server content here.
     */
    default void onServerInit() {
    }

    /**
     * Ran if the current environment is a server.
     * <p>On Fabric this runs in Borealib's mod entrypoint and on Forge wraps <code>FMLDedicatedServerSetupEvent</code>. Register non-threadsafe server content here.
     * <p>Registry contents of Borealib-dependent mods should be guaranteed to be registered at this point.
     *
     * @param dispatcher The parallel dispatcher to use for deferring non-threadsafe code
     */
    default void onServerPostInit(ParallelDispatcher dispatcher) {
    }

    /**
     * Used during data generation to add data providers. If this is used, make sure the fabric initializer is set as a data generator entrypoint in <code>fabric.mod.json</code>.
     *
     * @param generator The data "generator" to add providers to
     */
    default void onDataInit(BorealibDataGenerator generator) {
    }

    /**
     * Used during data generation to build contents for dynamic registries whose contents can only be defined via data.
     *
     * @param builder The {@link RegistrySetBuilder} to hook dynamic registry bootstraps to
     */
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

    static ModLoaderService byId(String id) {
        return ServiceLoader.load(ModLoaderService.class)
                .stream()
                .filter(p -> p.get().id().equals(id))
                .findFirst()
                .map(ServiceLoader.Provider::get)
                .orElseThrow(() -> new IllegalStateException("Couldn't find mod service with the id" + id));
    }

    static Stream<ModLoaderService> getAll() {
        return ServiceLoader.load(ModLoaderService.class).stream().map(ServiceLoader.Provider::get);
    }
}

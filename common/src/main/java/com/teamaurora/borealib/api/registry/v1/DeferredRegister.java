package com.teamaurora.borealib.api.registry.v1;

import com.teamaurora.borealib.impl.registry.DeferredRegisterImpl;
import com.teamaurora.borealib.impl.registry.VanillaDeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The main means to register content.
 * <p>Deferred registers are automatically handled by each platform and no other work is needed than defining the register and running the {@link #register()} method on common init.
 *
 * @param <T> The top level of the registry type
 */
public interface DeferredRegister<T> extends Iterable<RegistryReference<T>> {

    static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return DeferredRegisterImpl.create(registryKey, modId);
    }

    static <T> DeferredRegister<T> create(RegistryView<T> registryView, String modId) {
        return create(registryView.key(), modId);
    }

    static <T> DeferredRegister<T> customWriter(SimpleCustomRegistry<T> registry, String modId) {
        return new VanillaDeferredRegister<>(registry.unwrap(), modId);
    }

    String id();

    ResourceKey<? extends Registry<T>> getRegistryKey();

    <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object);

    default <R extends T> RegistryReference<R> register(String path, Supplier<? extends R> object) {
        return this.register(new ResourceLocation(this.id(), path), object);
    }

    default <R extends T> RegistryReference<R> registerConditional(ResourceLocation name, Supplier<? extends R> dummy, Supplier<? extends R> object, boolean register) {
        return this.register(name, register ? object : dummy);
    }

    default <R extends T> RegistryReference<R> registerConditional(String path, Supplier<? extends R> dummy, Supplier<? extends R> object, boolean register) {
        return this.register(path, register ? object : dummy);
    }

    default Stream<RegistryReference<T>> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    /**
     * Initializes the classes where registry elements are contained to ensure they are loaded. Has no effect in code.
     */
    default void register() {}
}

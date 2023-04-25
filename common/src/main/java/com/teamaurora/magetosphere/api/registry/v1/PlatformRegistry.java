package com.teamaurora.magetosphere.api.registry.v1;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.Lifecycle;
import com.teamaurora.magetosphere.impl.registry.VanillaPlatformRegistry;
import net.minecraft.core.IdMap;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * The base class for registering cross-platform objects. Also properly supports Forge's registry API rather than using vanilla {@link net.minecraft.core.registries.BuiltInRegistries}.
 *
 * @param <T> The top level of the registry type
 */
public interface PlatformRegistry<T> extends Keyable, IdMap<T> {

    /**
     * Creates a simple {@link PlatformRegistry} wrapping a discrete vanilla registry (not tracked by {@link RegistryManager}).
     * <p>This should be used in scenarios where only basic functionality is required.
     * <p>If features such as automatic syncing between servers and clients are needed, use {@link RegistryManager#createRegistry(ResourceLocation, RegistryProperties)} instead.
     *
     * @param id  The name of the registry
     * @param <T> The registry type
     * @return A new simple {@link PlatformRegistry}
     */
    static <T> PlatformRegistry<T> createStandAlone(ResourceLocation id) {
        return new VanillaPlatformRegistry<>(new MappedRegistry<>(ResourceKey.createRegistryKey(id), Lifecycle.stable()));
    }

    /**
     * Queues an object to be registered.
     *
     * @param name        The name of the object to register, ensure the namespace is your own mod id
     * @param object      The object to register
     * @param <R>         The object type
     * @return A reference to the registered object
     */
    <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object);

    /**
     * Queues an object to be registered based on a condition.
     *
     * @param name        The name of the object to register, ensure the namespace is your own mod id
     * @param dummy       The object to register if the condition is false
     * @param object      The object to register if the condition is true
     * @param <R>         The object type
     * @return A reference to the registered object
     */
    default <R extends T> RegistryReference<R> registerConditional(ResourceLocation name, Supplier<? extends R> dummy, Supplier<? extends R> object, boolean register) {
        return this.register(name, register ? object : dummy);
    }

    /**
     * @return The codec to identify objects of this registry
     */
    Codec<T> byNameCodec();

    /**
     * Retrieves the key for the specified value.
     *
     * @param value The value to get the key for
     * @return A key for that value or <code>null</code> if this registry doesn't contain that value
     */
    @Nullable
    ResourceLocation getKey(T value);

    /**
     * Retrieves the id for the specified value. This can only be used for a custom registry.
     *
     * @param value The value to get the id for
     * @return An id for that value or <code>null</code> if this registry doesn't contain that id
     */
    int getId(@Nullable T value);

    /**
     * Retrieves the value for the specified id. This can only be used for a custom registry.
     *
     * @param id The id to get the value for
     * @return A value for that id or <code>null</code> if this registry doesn't contain a value with that id
     */
    @Nullable
    T byId(int id);

    /**
     * @return The size of the registry
     */
    @Override
    int size();

    /**
     * Retrieves the value for the specified key.
     *
     * @param name The key to get the value for
     * @return A value for that key or <code>null</code> if this registry doesn't contain a value with that name
     */
    @Nullable
    T get(@Nullable ResourceLocation name);

    /**
     * Retrieves the value for the specified key.
     *
     * @param name The key to get the value for
     * @return A value for that key
     */
    default Optional<T> getOptional(@Nullable ResourceLocation name) {
        return Optional.ofNullable(this.get(name));
    }

    /**
     * @return The key of this registry
     */
    ResourceKey<? extends Registry<T>> key();

    /**
     * Retrieves the value for the specified id.
     *
     * @param id The id to get the value for
     * @return A value for that id
     */
    default Optional<T> byIdOptional(int id) {
        return Optional.ofNullable(this.byId(id));
    }

    /**
     * @return A set of all registered keys in the registry
     */
    Set<ResourceLocation> keySet();

    /**
     * @return A set of all registered entries in the entry
     */
    Set<Map.Entry<ResourceKey<T>, T>> entrySet();

    /**
     * @return A stream of all values in the registry
     */
    default Stream<T> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    /**
     * Checks to see if a value with the specified name exists.
     *
     * @param name The name of the key to get
     * @return Whether that value exists
     */
    boolean containsKey(ResourceLocation name);
}

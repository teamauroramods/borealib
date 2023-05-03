package com.teamaurora.magnetosphere.api.registry.v1;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A reference to a registered object. This wraps <code>RegistryObject</code> on Forge.
 *
 * @param <T> The object type
 */
public interface RegistryReference<T> extends Supplier<T> {

    /**
     * @return The actual registry element
     */
    @Override
    T get();

    /**
     * @return Whether the object has been registered
     */
    boolean isPresent();

    /**
     * @return The name of the object
     */
    ResourceLocation getId();

    /**
     * @return A {@link ResourceKey} pointing to the object
     */
    ResourceKey<T> getKey();

    /**
     * @return The object as a vanilla {@link Holder} if that's needed for some reason
     */
    Optional<Holder<T>> getHolder();


    /**
     * Adds code to run when the object registers
     *
     * @param consumer The code to run
     */
    void listen(Consumer<T> consumer);
}

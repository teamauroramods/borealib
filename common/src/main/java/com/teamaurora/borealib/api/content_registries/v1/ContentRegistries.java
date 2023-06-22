package com.teamaurora.borealib.api.content_registries.v1;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.registry.v1.RegistryView;
import com.teamaurora.borealib.impl.content_registries.ContentRegistriesImpl;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Manages {@link ContentRegistry} instances.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface ContentRegistries {

    /**
     * Gets a content registry with the specified location.
     *
     * @param registryId The id to find the registry by
     * @param <T>        The registry type
     * @param <R>        The associated value type
     * @return A content registry with the id if it exists, otherwise <code>null</code>
     */
    @Nullable
    static <T, R> ContentRegistry<T, R> get(ResourceLocation registryId) {
        return ContentRegistriesImpl.get(registryId);
    }

    /**
     * Creates a content registry with the given properties and a listener to be called upon reload.
     *
     * @param registryId     The id to identify the content registry by in file directories
     * @param parentRegistry The parent registry of the entry type
     * @param elementCodec   The codec to serialize and deserialize values
     * @param onReload       A listener to fire when the content registry is done reloading
     * @param <T>            The registry type
     * @param <R>            The associated value type
     * @return A new {@link ContentRegistry} with the given properties
     */
    static <T, R> ContentRegistry<T, R> create(ResourceLocation registryId, RegistryView<T> parentRegistry, Codec<R> elementCodec, @Nullable Consumer<ContentRegistry<T, R>> onReload) {
        return ContentRegistriesImpl.create(registryId, parentRegistry, elementCodec, onReload);
    }

    /**
     * Creates a content registry with the given properties.
     *
     * @param registryId     The id to identify the content registry by in file directories
     * @param parentRegistry The parent registry of the entry type
     * @param elementCodec   The codec to serialize and deserialize values
     * @param <T>            The registry type
     * @param <R>            The associated value type
     * @return A new {@link ContentRegistry} with the given properties
     */
    static <T, R> ContentRegistry<T, R> create(ResourceLocation registryId, RegistryView<T> parentRegistry, Codec<R> elementCodec) {
        return create(registryId, parentRegistry, elementCodec, null);
    }
}
package com.teamaurora.borealib.api.content_registries.v1;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.registry.v1.RegistryView;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Represents any map-like, serverside registry that associates a <b>non-dynamic</b> registry element with an arbitrary value. Registry contents can be reloaded from JSON files in datapacks.
 * <p>The file location is <code>data/[namespace]/content_registries/[parent]/[path].json</code>,
 * <p>where <code>parent</code> is either the parent registry path if it is vanilla, or the full location separated by directory if modded.
 *
 * @param <T> The registry element type
 * @param <R> The value type
 * @author ebo2022
 * @since 1.0
 */
public interface ContentRegistry<T, R> {

    /**
     * @return The registry controlling the entries
     */
    RegistryView<T> registry();

    /**
     * @return The id of this registry
     */
    ResourceLocation id();

    /**
     * Gets the value assigned to an entry, also looking for tag entries.
     *
     * @param entry The entry to get a value for
     * @return The assigned value if it exists for a tag or the entry
     */
    @Nullable
    R get(T entry);

    /**
     * Directly gets the value assigned to an entry, ignoring any tags.
     *
     * @param entry The entry to get a value for
     * @return The assigned value if it exists
     */
    @Nullable
    R getDirect(T entry);

    /**
     * Gets the value assigned to a tag key.
     *
     * @param tagKey The tag to get a value for
     * @return The assigned value if it exists
     */
    @Nullable
    R getByTag(TagKey<T> tagKey);

    /**
     * @return The key of the parent registry
     */
    default ResourceKey<? extends Registry<T>> key() {
        return this.registry().key();
    }

    /**
     * @return A set of all associated registry entries and values
     */
    Set<Entry<T, R>> fullEntrySet();

    /**
     * @return A set of all associated registry entries and values NOT defined via a tag
     */
    Set<Entry<T, R>> directEntrySet();

    /**
     * @return A set of all associated registry tags and values
     */
    Set<TagEntry<T, R>> tagEntrySet();

    /**
     * @return A set of all registered entries
     */
    Set<T> keySet();

    /**
     * @return A set of all registered values
     */
    Set<R> values();

    /**
     * @return A codec to serialize and deserialize values
     */
    Codec<R> elementCodec();

    record Entry<T, R>(T object, R value) {}

    record TagEntry<T, R>(TagKey<T> object, R value) {}
}

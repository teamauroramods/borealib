package com.teamaurora.borealib.api.registry.v1;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Holds all tag data relating to a static registry. This is preferred over using vanilla Holder methods where possible.
 * <p>Based on Forge's tag system.
 *
 * @param <T> The type of registry object
 * @author ebo2022
 * @since 1.0
 */
public interface RegistryTagManager<T> extends Iterable<RegistryTagManager.TagData<T>> {

    /**
     * Gets tracked data for the given tag key or creates it if it doesn't exist yet.
     * <p>All future query operations to the same tag key will yield the same data.
     *
     * @param tagKey
     * @return The persistent data for the tag key
     */
    TagData<T> getTag(TagKey<T> tagKey);

    /**
     * Gets data containing what tags the specified object has.
     *
     * @param object The object to get tag data for
     * @return The reverse tag data for the object
     */
    Optional<ReverseTagData<T>> getReverseTag(T object);

    /**
     * Checks to see if the specified tag key is known to the manager.
     * <p>New data is not created if the data isn't present.
     *
     * @param tagKey The tag key to check
     * @return Whether the tag key is tracked by the manager and currently bound to any values
     */
    boolean isKnownTagName(TagKey<T> tagKey);

    /**
     * @return A stream of all stored tag data
     */
    Stream<TagData<T>> stream();

    /**
     * @return A stream of all stored tag names
     */
    Stream<TagKey<T>> getTagNames();

    /**
     * Creates a new {@link TagKey} based on the parent registry and location.
     *
     * @param name The name of the tag to create
     * @return The new tag key
     */
    TagKey<T> createTagKey(ResourceLocation name);

    /**
     * Creates a new {@link TagKey} based on the parent registry and location.
     * <p>If there are no loaded values from datapacks, the default values will be loaded instead.
     *
     * @param name          The name of the tag to create
     * @param defaultValues The default values to use when no others are present
     * @return The new tag key
     */
    TagKey<T> createOptionalTagKey(ResourceLocation name, Set<? extends Supplier<T>> defaultValues);

    /**
     * Adds default values for the specified {@link TagKey} when there are no others from datapacks present, useful if a server doesn't provide a desired tag.
     *
     * @param tagKey        The tag key to add default values for
     * @param defaultValues The default values to use when no others are present
     */
    void addOptionalTagDefaults(TagKey<T> tagKey, Set<? extends Supplier<T>> defaultValues);

    /**
     * Holds a group of elements associated with a {@link TagKey}, bound on world load.
     * <p>This data is persistent and is viable for long-term storage after its first use.
     *
     * @param <T> The registry object type
     */
    interface TagData<T> extends Iterable<T> {

        /**
         * @return The {@link TagKey} the data is for
         */
        TagKey<T> getKey();

        /**
         * @return A stream of all values in the tag
         */
        Stream<T> stream();

        /**
         * @return Whether the tag is empty
         */
        boolean isEmpty();

        /**
         * @return How many elements are part of the tag
         */
        int size();

        /**
         * Checks whether the specified object is in the tag.
         *
         * @param object The object to check
         * @return Whether the object is part of the tag data
         */
        boolean contains(T object);

        /**
         * Gets a random element from the tag.
         *
         * @param random The random to compute the element
         * @return The randomly selected element
         */
        Optional<T> getRandomElement(RandomSource random);

        /**
         * @return Whether the tag is bound to any values
         */
        boolean isBound();
    }

    /**
     * Object-specific data that is aware of what tags it is in.
     *
     * @param <T> The registry object type
     */
    interface ReverseTagData<T> {

        /**
         * @return The tags the object is in
         */
        Stream<TagKey<T>> getTagKeys();

        /**
         * Checks if the object has the specified tag.
         *
         * @param tagKey The tag key to check
         * @return Whether the object is part of the tag
         */
        boolean containsTag(TagKey<T> tagKey);

        /**
         * Checks if the object is in the specified tag data.
         *
         * @param tag The tag data to check
         * @return Whether the object is part of the data
         */
        default boolean containsTag(TagData<T> tag) {
            return this.containsTag(tag.getKey());
        }
    }
}

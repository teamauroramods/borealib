package com.teamaurora.borealib.api.datagen.v1;

import com.google.gson.JsonObject;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * A {@link DataProvider} extension with support for adding custom {@link ResourceConditionProvider}s.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface ConditionalDataProvider extends DataProvider {

    /**
     * Add conditions to generate with the object of the specified id.
     *
     * @param id        The id of the object to add conditions for
     * @param providers The conditions to add
     */
    void addConditions(ResourceLocation id, ResourceConditionProvider... providers);

    /**
     * Injects the conditions into the serialized JSON object.
     * <p>This should be ran in the data provider's implementation of {@link #run(CachedOutput)}.
     *
     * @param id   The id to get resource conditions by
     * @param json The object to add conditions to
     */
    void injectConditions(ResourceLocation id, JsonObject json);
}

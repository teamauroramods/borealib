package com.teamaurora.borealib.api.datagen.v1.providers.model;

import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**
 * A helper class for generating new model templates and texture slots.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface ModelGeneratorHelper {

    TextureMapping EMPTY_TEXTURE_MAPPING = new TextureMapping();

    /**
     * Creates a {@link ModelTemplate} with the specified texture slots.
     *
     * @param textureSlots The texture slots to use
     * @return A new model template
     */
    static ModelTemplate template(TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.empty(), Optional.empty(), textureSlots);
    }

    /**
     * Creates a {@link ModelTemplate} with the specified texture slots and parent model.
     *
     * @param parentLocation The parent model
     * @param textureSlots   The texture slots to use
     * @return A new model template
     */
    static ModelTemplate template(ResourceLocation parentLocation, TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.of(parentLocation), Optional.empty(), textureSlots);
    }

    /**
     * Creates a {@link ModelTemplate} with the specified texture slots, parent model and prefix.
     *
     * @param parentLocation The parent model
     * @param suffix         The suffix to use
     * @param textureSlots   The texture slots to use
     * @return A new model template
     */
    static ModelTemplate template(ResourceLocation parentLocation, String suffix, TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.of(parentLocation), Optional.of(suffix), textureSlots);
    }

    /**
     * Creates a new {@link TextureSlot} with the specified key.
     *
     * @param key The id of the slot
     * @return A new texture slot
     */
    static TextureSlot slot(String key) {
        return new TextureSlot(key, null);
    }

    /**
     * Creates a new {@link TextureSlot} with the specified key and parent.
     *
     * @param key    The id of the slot
     * @param parent The parent slot
     * @return A new texture slot
     */
    static TextureSlot slot(String key, TextureSlot parent) {
        return new TextureSlot(key, parent);
    }
}

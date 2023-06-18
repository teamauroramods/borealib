package com.teamaurora.borealib.api.biome.v1.modifier;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.core.registry.BorealibRegistries;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents an action a biome modifier can take on its selected biomes.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface BiomeModifierAction extends Consumer<BiomeInfo.Mutable> {

    Codec<BiomeModifierAction> CODEC = BorealibRegistries.BIOME_MODIFIER_ACTION_TYPES.byNameCodec().dispatch(BiomeModifierAction::type, Function.identity());

    /**
     * Applies the action to the given biome.
     *
     * @param info Context to modify the biome
     */
    @Override
    void accept(BiomeInfo.Mutable info);

    /**
     * @return A codec to serialize and deserialize instances of this action. It should be registered to {@link BorealibRegistries#BIOME_MODIFIER_ACTION_TYPES}
     */
    Codec<? extends BiomeModifierAction> type();

    /**
     * @return The stage to run the action at
     */
    Stage applicationStage();

    /**
     * Stages of biome modification.
     *
     * @since 1.0
     */
    enum Stage {

        /**
         * Used for adding properties; this runs first.
         */
        ADDITIONS,

        /**
         * Used for removing properties from biomes.
         */
        REMOVALS,

        /**
         * Used for combinations of additions and removals to replace properties. The amount of additions should be equal to that of the removals.
         */
        REPLACEMENTS,

        /**
         * Used for wide-reaching post-processing of biome properties; this runs last.
         */
        POST_PROCESSING
    }
}

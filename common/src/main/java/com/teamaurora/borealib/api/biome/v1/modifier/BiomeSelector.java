package com.teamaurora.borealib.api.biome.v1.modifier;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Selects what biomes a biome modifier should apply to.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface BiomeSelector extends Predicate<BiomeSelector.Context> {

    Codec<BiomeSelector> CODEC = BorealibRegistries.BIOME_SELECTOR_TYPES.byNameCodec().dispatch(BiomeSelector::type, Function.identity());

    /**
     * Tests the selector against the given biome.
     *
     * @param context The context to help choose whether to accept the current biome
     * @return Whether the modifier should be applied to the biome
     */
    @Override
    boolean test(Context context);

    /**
     * @return A codec to serialize and deserialize instances of this selector. It should be registered to {@link BorealibRegistries#BIOME_SELECTOR_TYPES}
     */
    Codec<? extends BiomeSelector> type();

    /**
     * Context to assist in choosing biomes.
     *
     * @since 1.0
     */
    interface Context {

        /**
         * @return Existing information for the biome that may or may not have been changed by other modifiers
         */
        BiomeInfo getExistingInfo();

        /**
         * @return The current biome
         */
        Holder<Biome> getBiome();

        /**
         * @return The key of the current biome
         */
        default ResourceKey<Biome> getBiomeKey() {
            // The biome holder should always be a reference so this is safe to do
            return this.getBiome().unwrapKey().orElseThrow();
        }

        /**
         * Checks if the specified structure can generate in the current biome.
         *
         * @param structure The structure to check
         * @return Whether the structure can generate in the biome
         */
        boolean hasStructure(ResourceKey<Structure> structure);

        /**
         * Checks if the current biome generates in the specified dimension.
         *
         * @param dimension The dimension to check
         * @return Whether the biome can spawn in the dimension
         */
        boolean generatesIn(ResourceKey<LevelStem> dimension);
    }
}

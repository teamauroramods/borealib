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

public interface BiomeSelector extends Predicate<BiomeSelector.Context> {

    Codec<BiomeSelector> CODEC = BorealibRegistries.BIOME_SELECTOR_TYPES.byNameCodec().dispatch(BiomeSelector::type, Function.identity());

    @Override
    boolean test(Context context);

    Codec<? extends BiomeSelector> type();

    interface Context {

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
         * @return Whether the biome can spawn in the dimention
         */
        boolean generatesIn(ResourceKey<LevelStem> dimension);
    }
}

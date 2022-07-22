package dev.tesseract.biomes.fabric;

import dev.tesseract.biomes.BiomeHelper;
import dev.tesseract.biomes.BiomeModifierRegistry;
import dev.tesseract.biomes.BiomeProperties;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public class BiomeDataImpl implements BiomeModifierRegistry.BiomeData {

    private final BiomeSelectionContext parent;

    public BiomeDataImpl(BiomeSelectionContext parent) {
        this.parent = parent;
    }

    @Override
    public Optional<ResourceLocation> getKey() {
        return Optional.ofNullable(this.parent.getBiomeKey().location());
    }

    @Override
    public BiomeProperties getProperties() {
        return BiomeHelper.getProperties(this.parent.getBiome());
    }

    @Override
    public boolean hasTag(TagKey<Biome> tag) {
        return this.parent.hasTag(tag);
    }
}

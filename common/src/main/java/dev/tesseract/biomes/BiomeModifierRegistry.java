package dev.tesseract.biomes;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.tesseract.api.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;


public final class BiomeModifierRegistry {

    private BiomeModifierRegistry() {
    }

    @ExpectPlatform
    public static void register(Phase phase, Predicate<BiomeData> selector, BiConsumer<BiomeData, BiomeProperties.Mutable> modifier) {
        Platform.error();
    }
    
    public interface BiomeData {
        Optional<ResourceLocation> getKey();
        
        BiomeProperties getProperties();
        
        boolean hasTag(TagKey<Biome> tag);
    }

    public enum Phase {
        ADDITIONS,
        REMOVALS,
        REPLACEMENTS,
        POST
    }
}

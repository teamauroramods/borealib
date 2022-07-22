package dev.tesseract.biomes.forge;

import com.mojang.serialization.Codec;
import dev.tesseract.biomes.BiomeModifierRegistry;
import dev.tesseract.biomes.BiomeModifierRegistry.BiomeData;
import dev.tesseract.biomes.BiomeProperties;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@ApiStatus.Internal
public class BiomeModifierRegistryImpl {

    private static final List<Pair<Predicate<BiomeData>, BiConsumer<BiomeData, BiomeProperties.Mutable>>> ADDITIONS = new ArrayList<>();
    private static final List<Pair<Predicate<BiomeData>, BiConsumer<BiomeData, BiomeProperties.Mutable>>> REMOVALS = new ArrayList<>();
    private static final List<Pair<Predicate<BiomeData>, BiConsumer<BiomeData, BiomeProperties.Mutable>>> REPLACEMENTS = new ArrayList<>();
    private static final List<Pair<Predicate<BiomeData>, BiConsumer<BiomeData, BiomeProperties.Mutable>>> POST = new ArrayList<>();

    @Nullable
    private static Codec<PollenBiomeModifier> EMPTY_MODIFIER_CODEC = null;

    public static void register(BiomeModifierRegistry.Phase phase, Predicate<BiomeData> selector, BiConsumer<BiomeData, BiomeProperties.Mutable> modifier) {
        switch (phase) {
            case ADDITIONS -> ADDITIONS.add(Pair.of(selector, modifier));
            case REMOVALS -> REMOVALS.add(Pair.of(selector, modifier));
            case REPLACEMENTS -> REPLACEMENTS.add(Pair.of(selector, modifier));
            case POST -> POST.add(Pair.of(selector, modifier));
            default -> throw new UnsupportedOperationException("Invalid modification phase: " + phase);
        }
    }

    private static class PollenBiomeModifier implements BiomeModifier {

        private static final PollenBiomeModifier INSTANCE = new PollenBiomeModifier();

        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            List<Pair<Predicate<BiomeData>, BiConsumer<BiomeData, BiomeProperties.Mutable>>> list = switch (phase) {
                case ADD -> ADDITIONS;
                case REMOVE -> REMOVALS;
                case MODIFY -> REPLACEMENTS;
                case AFTER_EVERYTHING -> POST;
                default -> null;
            };

            if (list == null) return;
            BiomeData data = new BiomeDataImpl(biome.unwrapKey(), builder);
            BiomeProperties.Mutable mutableBiome = new MutableBiomePropertiesImpl(builder);
            for (var pair : list) {
                if (pair.getLeft().test(data)) {
                    pair.getRight().accept(data, mutableBiome);
                }
            }
        }

        @Override
        public Codec<? extends BiomeModifier> codec() {
            if (EMPTY_MODIFIER_CODEC != null) {
                return EMPTY_MODIFIER_CODEC;
            } else {
                return Codec.unit(INSTANCE);
            }
        }
    }
}

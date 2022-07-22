package dev.tesseract.biomes.fabric;

import dev.tesseract.biomes.BiomeModifierRegistry;
import dev.tesseract.biomes.BiomeModifierRegistry.BiomeData;
import dev.tesseract.biomes.BiomeProperties;
import dev.tesseract.core.Tesseract;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@ApiStatus.Internal
public class BiomeModifierRegistryImpl {

    private static final ResourceLocation MODIFIER_LOCATION = new ResourceLocation(Tesseract.MOD_ID, "fabric_modifiers");

    private static final List<Pair<Predicate<BiomeData>, BiConsumer<BiomeData, BiomeProperties.Mutable>>> ADDITIONS = new ArrayList<>();
    private static final List<Pair<Predicate<BiomeData>, BiConsumer<BiomeData, BiomeProperties.Mutable>>> REMOVALS = new ArrayList<>();
    private static final List<Pair<Predicate<BiomeData>, BiConsumer<BiomeData, BiomeProperties.Mutable>>> REPLACEMENTS = new ArrayList<>();
    private static final List<Pair<Predicate<BiomeData>, BiConsumer<BiomeData, BiomeProperties.Mutable>>> POST = new ArrayList<>();

    static {
        BiomeModification modification = BiomeModifications.create(MODIFIER_LOCATION);
        addModifiers(modification, ModificationPhase.ADDITIONS, ADDITIONS);
        addModifiers(modification, ModificationPhase.REMOVALS, REMOVALS);
        addModifiers(modification, ModificationPhase.REPLACEMENTS, REPLACEMENTS);
        addModifiers(modification, ModificationPhase.POST_PROCESSING, POST);
    }

    public static void register(BiomeModifierRegistry.Phase phase, Predicate<BiomeData> selector, BiConsumer<BiomeData, BiomeProperties.Mutable> modifier) {
        switch (phase) {
            case ADDITIONS -> ADDITIONS.add(Pair.of(selector, modifier));
            case REMOVALS -> REMOVALS.add(Pair.of(selector, modifier));
            case REPLACEMENTS -> REPLACEMENTS.add(Pair.of(selector, modifier));
            case POST -> POST.add(Pair.of(selector, modifier));
            default -> throw new UnsupportedOperationException("Invalid modification phase: " + phase);
        }
    }

    private static void addModifiers(BiomeModification modification, ModificationPhase phase, List<Pair<Predicate<BiomeData>, BiConsumer<BiomeData, BiomeProperties.Mutable>>> list) {
        modification.add(phase, selectionContext -> true, (selectionContext, modificationContext) -> {
            BiomeData data = new BiomeDataImpl(selectionContext);
            BiomeProperties.Mutable properties = BiomeHelperImpl.newMutableProperties(selectionContext.getBiome(), modificationContext);
            for (var pair : list) {
                if (pair.getLeft().test(data)) {
                    pair.getRight().accept(data, properties);
                }
            }
        });
    }
}

package com.teamaurora.borealib.impl.biome.modifier.fabric;

import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifier;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifierAction;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeSelector;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import com.teamaurora.borealib.impl.biome.modifier.info.fabric.BiomeInfoImpl;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Predicate;

@ApiStatus.Internal
public final class FabricBiomeModifierLoader {

    private FabricBiomeModifierLoader() {
    }

    public static void runModifiers(RegistryAccess access) {
        Registry<BiomeModifier> registry = access.registryOrThrow(BorealibRegistries.BIOME_MODIFIERS);
        registry.entrySet().forEach(entry -> {
            BiomeModification fabricModification = BiomeModifications.create(entry.getKey().location());
            Predicate<BiomeSelectionContext> sharedSelector = context -> entry.getValue().selector().test(new BiomeSelectorContextImpl(context));
            entry.getValue().actions().forEach(action -> {
                for (BiomeModifierAction.Stage stage : BiomeModifierAction.Stage.values()) {
                    if (stage == action.applicationStage()) {
                        fabricModification.add(wrapPhase(stage), sharedSelector, (selectorCtx, ctx) -> action.accept(new BiomeInfoImpl.Mutable(selectorCtx.getBiome(), ctx, selectorCtx)));
                    }
                }
            });
        });
        Borealib.LOGGER.info("Applied " + registry.size() + " data-driven biome modifiers");
    }

    private static ModificationPhase wrapPhase(BiomeModifierAction.Stage stage) {
        return switch (stage) {
            case ADDITIONS -> ModificationPhase.ADDITIONS;
            case REMOVALS -> ModificationPhase.REMOVALS;
            case REPLACEMENTS -> ModificationPhase.REPLACEMENTS;
            case POST_PROCESSING -> ModificationPhase.POST_PROCESSING;
        };
    }

    private static class BiomeSelectorContextImpl implements BiomeSelector.Context {

        private final BiomeSelectionContext context;
        private final BiomeInfo info;

        private BiomeSelectorContextImpl(BiomeSelectionContext context) {
            this.context = context;
            this.info = BiomeInfoImpl.wrap(context);
        }

        @Override
        public BiomeInfo getExistingInfo() {
            return this.info;
        }

        @Override
        public Holder<Biome> getBiome() {
            return this.context.getBiomeRegistryEntry();
        }

        @Override
        public boolean hasStructure(ResourceKey<Structure> structure) {
            return this.context.validForStructure(structure);
        }

        @Override
        public boolean generatesIn(ResourceKey<LevelStem> dimension) {
            return this.context.canGenerateIn(dimension);
        }
    }
}
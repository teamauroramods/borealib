package com.teamaurora.borealib.impl.biome.modifier.forge;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.base.v1.util.forge.ForgeHelper;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifier;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifierAction;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeSelector;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import com.teamaurora.borealib.impl.biome.modifier.info.forge.BiomeInfoImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ForgeBiomeModifierLoader {

    private ForgeBiomeModifierLoader() {
    }

    public static void init() {
        IEventBus bus = ForgeHelper.getEventBus(Borealib.MOD_ID);
        bus.<RegisterEvent>addListener(event -> {
            event.register(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, helper -> helper.register(Borealib.location("forge_biome_modifier_handler"), Impl.CODEC));
        });
    }

    private static net.minecraftforge.common.world.BiomeModifier.Phase wrapPhase(BiomeModifierAction.Stage phase) {
        return switch (phase) {
            case ADDITIONS -> net.minecraftforge.common.world.BiomeModifier.Phase.ADD;
            case REMOVALS -> net.minecraftforge.common.world.BiomeModifier.Phase.REMOVE;
            case REPLACEMENTS -> net.minecraftforge.common.world.BiomeModifier.Phase.MODIFY;
            case POST_PROCESSING -> net.minecraftforge.common.world.BiomeModifier.Phase.AFTER_EVERYTHING;
        };
    }

    private static class Impl implements net.minecraftforge.common.world.BiomeModifier {

        private static final Impl INSTANCE = new Impl();
        private static final Codec<Impl> CODEC = Codec.unit(INSTANCE);

        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            // Inferred that this is present as modifiers are run during server startup
            RegistryAccess registryAccess = Platform.getRegistryAccess().orElseThrow();
            Registry<BiomeModifier> registry = registryAccess.registryOrThrow(BorealibRegistries.BIOME_MODIFIERS);
            BiomeInfo.Mutable info = new BiomeInfoImpl(builder);
            BiomeSelector.Context selectionContext = new BiomeSelectorContextImpl(biome, info, registryAccess);
            registry.forEach(modifier -> {
                if (modifier.selector().test(selectionContext)) {
                    modifier.actions().forEach(action -> {
                        if (phase == wrapPhase(action.applicationStage())) {
                            action.accept(info);
                        }
                    });
                }
            });
        }

        @Override
        public Codec<? extends net.minecraftforge.common.world.BiomeModifier> codec() {
            return CODEC;
        }
    }

    private static class BiomeSelectorContextImpl implements BiomeSelector.Context {

        private final Holder<Biome> biome;
        private final BiomeInfo info;
        private final RegistryAccess registryAccess;

        public BiomeSelectorContextImpl(Holder<Biome> biome, BiomeInfo info, RegistryAccess registryAccess) {
            this.biome = biome;
            this.info = info;
            this.registryAccess = registryAccess;
        }

        @Override
        public BiomeInfo getExistingInfo() {
            return this.info;
        }

        @Override
        public Holder<Biome> getBiome() {
            return this.biome;
        }

        @Override
        public boolean hasStructure(ResourceKey<Structure> structure) {
            Structure structureInstance = this.registryAccess.registryOrThrow(Registries.STRUCTURE).get(structure);
            return structureInstance.biomes().contains(this.getBiome());
        }

        @Override
        public boolean generatesIn(ResourceKey<LevelStem> dimension) {
            LevelStem levelStem = this.registryAccess.registryOrThrow(Registries.LEVEL_STEM).get(dimension);
            if (dimension == null)
                return false;
            return levelStem.generator().getBiomeSource().possibleBiomes().stream().anyMatch(entry -> entry.value() == biome.value());
        }
    }
}

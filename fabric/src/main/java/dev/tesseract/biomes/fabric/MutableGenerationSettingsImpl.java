package dev.tesseract.biomes.fabric;

import com.mojang.datafixers.util.Either;
import dev.tesseract.biomes.GenerationSettings;
import dev.tesseract.biomes.impl.ImmutableGenerationSettings;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MutableGenerationSettingsImpl extends ImmutableGenerationSettings implements GenerationSettings.Mutable {

    private final BiomeModificationContext.GenerationSettingsContext parent;

    public MutableGenerationSettingsImpl(Biome biome, BiomeModificationContext.GenerationSettingsContext parent) {
        super(biome);
        this.parent = parent;
    }

    @Override
    public Mutable addFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature) {
        Either<ResourceKey<PlacedFeature>, PlacedFeature> unwrapped = feature.unwrap();
        if (unwrapped.left().isPresent()) {
            this.parent.addFeature(decoration, unwrapped.left().get());
        } else {
            this.parent.addBuiltInFeature(decoration, unwrapped.right().get());
        }
        return this;
    }

    @Override
    public Mutable addCarver(GenerationStep.Carving carving, Holder<ConfiguredWorldCarver<?>> feature) {
        Either<ResourceKey<ConfiguredWorldCarver<?>>, ConfiguredWorldCarver<?>> unwrap = feature.unwrap();
        // ensure the feature is added
        if (unwrap.left().isPresent()) {
            this.parent.addCarver(carving, unwrap.left().get());
        } else {
            this.parent.addBuiltInCarver(carving, unwrap.right().get());
        }
        return this;
    }

    @Override
    public Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature) {
        this.parent.removeFeature(decoration, feature);
        return this;
    }

    @Override
    public Mutable removeCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature) {
        this.parent.removeCarver(carving, feature);
        return this;
    }
}

package com.teamaurora.borealib.api.registry.v1.extended;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public final class FeatureRegistryProvider extends ExtendedRegistryWrapperProvider<Feature<?>> {

    private final RegistryWrapper.Provider<FoliagePlacerType<?>> foliagePlacerTypeRegistry;
    private final RegistryWrapper.Provider<TrunkPlacerType<?>> trunkPlacerTypeRegistry;
    private final RegistryWrapper.Provider<RootPlacerType<?>> rootPlacerTypeRegistry;
    private final RegistryWrapper.Provider<TreeDecoratorType<?>> treeDecoratorTypeRegistry;

    private FeatureRegistryProvider(RegistryWrapper.Provider<Feature<?>> parent) {
        super(parent);
        this.foliagePlacerTypeRegistry = RegistryWrapper.provider(Registries.FOLIAGE_PLACER_TYPE, parent.owner());
        this.trunkPlacerTypeRegistry = RegistryWrapper.provider(Registries.TRUNK_PLACER_TYPE, parent.owner());
        this.rootPlacerTypeRegistry = RegistryWrapper.provider(Registries.ROOT_PLACER_TYPE, parent.owner());
        this.treeDecoratorTypeRegistry = RegistryWrapper.provider(Registries.TREE_DECORATOR_TYPE, parent.owner());
    }

    public static FeatureRegistryProvider create(String owner) {
        return new FeatureRegistryProvider(RegistryWrapper.provider(Registries.FEATURE, owner));
    }

    public <I extends FoliagePlacer> RegistryReference<FoliagePlacerType<I>> registerFoliagePlacerType(String id, Codec<I> codec) {
        return this.foliagePlacerTypeRegistry.register(id, () -> new FoliagePlacerType<>(codec));
    }

    public <I extends FoliagePlacer> RegistryReference<FoliagePlacerType<I>> registerFoliagePlacerType(ResourceLocation id, Codec<I> codec) {
        return this.foliagePlacerTypeRegistry.register(id, () -> new FoliagePlacerType<>(codec));
    }

    public <I extends TrunkPlacer> RegistryReference<TrunkPlacerType<I>> registerTrunkPlacerType(String id, Codec<I> codec) {
        return this.trunkPlacerTypeRegistry.register(id, () -> new TrunkPlacerType<>(codec));
    }

    public <I extends TrunkPlacer> RegistryReference<TrunkPlacerType<I>> registerTrunkPlacerType(ResourceLocation id, Codec<I> codec) {
        return this.trunkPlacerTypeRegistry.register(id, () -> new TrunkPlacerType<>(codec));
    }

    public <I extends RootPlacer> RegistryReference<RootPlacerType<I>> registerRootPlacerType(String id, Codec<I> codec) {
        return this.rootPlacerTypeRegistry.register(id, () -> new RootPlacerType<>(codec));
    }

    public <I extends RootPlacer> RegistryReference<RootPlacerType<I>> registerRootPlacerType(ResourceLocation id, Codec<I> codec) {
        return this.rootPlacerTypeRegistry.register(id, () -> new RootPlacerType<>(codec));
    }

    public <I extends TreeDecorator> RegistryReference<TreeDecoratorType<I>> registerTreeDecoratorType(String id, Codec<I> codec) {
        return this.treeDecoratorTypeRegistry.register(id, () -> new TreeDecoratorType<>(codec));
    }

    public <I extends TreeDecorator> RegistryReference<TreeDecoratorType<I>> registerTreeDecoratorType(ResourceLocation id, Codec<I> codec) {
        return this.treeDecoratorTypeRegistry.register(id, () -> new TreeDecoratorType<>(codec));
    }

    public RegistryWrapper.Provider<FoliagePlacerType<?>> getFoliagePlacerTypeRegistry() {
        return this.foliagePlacerTypeRegistry;
    }

    public RegistryWrapper.Provider<RootPlacerType<?>> getRootPlacerTypeRegistry() {
        return this.rootPlacerTypeRegistry;
    }

    public RegistryWrapper.Provider<TreeDecoratorType<?>> getTreeDecoratorTypeRegistry() {
        return this.treeDecoratorTypeRegistry;
    }

    public RegistryWrapper.Provider<TrunkPlacerType<?>> getTrunkPlacerTypeRegistry() {
        return this.trunkPlacerTypeRegistry;
    }
}

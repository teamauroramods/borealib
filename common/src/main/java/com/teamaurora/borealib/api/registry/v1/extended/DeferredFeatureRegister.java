package com.teamaurora.borealib.api.registry.v1.extended;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.registry.v1.DeferredRegister;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
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

public final class DeferredFeatureRegister extends ExtendedDeferredRegister<Feature<?>> {

    private final DeferredRegister<FoliagePlacerType<?>> foliagePlacerTypeRegistry;
    private final DeferredRegister<TrunkPlacerType<?>> trunkPlacerTypeRegistry;
    private final DeferredRegister<RootPlacerType<?>> rootPlacerTypeRegistry;
    private final DeferredRegister<TreeDecoratorType<?>> treeDecoratorTypeRegistry;

    private DeferredFeatureRegister(DeferredRegister<Feature<?>> parent) {
        super(parent);
        this.foliagePlacerTypeRegistry = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, parent.id());
        this.trunkPlacerTypeRegistry = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, parent.id());
        this.rootPlacerTypeRegistry = DeferredRegister.create(Registries.ROOT_PLACER_TYPE, parent.id());
        this.treeDecoratorTypeRegistry = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, parent.id());
    }

    public static DeferredFeatureRegister create(String modId) {
        return new DeferredFeatureRegister(DeferredRegister.create(Registries.FEATURE, modId));
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

    public DeferredRegister<FoliagePlacerType<?>> getFoliagePlacerTypeRegistry() {
        return this.foliagePlacerTypeRegistry;
    }

    public DeferredRegister<RootPlacerType<?>> getRootPlacerTypeRegistry() {
        return this.rootPlacerTypeRegistry;
    }

    public DeferredRegister<TreeDecoratorType<?>> getTreeDecoratorTypeRegistry() {
        return this.treeDecoratorTypeRegistry;
    }

    public DeferredRegister<TrunkPlacerType<?>> getTrunkPlacerTypeRegistry() {
        return this.trunkPlacerTypeRegistry;
    }
}

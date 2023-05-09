package com.teamaurora.magnetosphere.api.block.set.wood;

import com.teamaurora.magnetosphere.api.BlockUtils;
import com.teamaurora.magnetosphere.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.magnetosphere.api.block.set.variant.BlockVariant;
import com.teamaurora.magnetosphere.api.content_registries.v1.FlammabilityRegistry;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;

public final class WoodVariants {

    private static final TriConsumer<ModLoaderService.ParallelDispatcher, WoodSet, RegistryReference<Block>> LOG_FLAMMMABLE = (dispatcher, woodSet, block) ->
            dispatcher.enqueueWork(() -> FlammabilityRegistry.register(5, 5, block.get()));
    private static final TriConsumer<ModLoaderService.ParallelDispatcher, WoodSet, RegistryReference<Block>> WOOD_FLAMMABLE = (dispatcher, woodSet, block) ->
            dispatcher.enqueueWork(() -> FlammabilityRegistry.register(5, 20, block.get()));

    public static final BlockVariant<WoodSet> STRIPPED_WOOD = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(barkColors(set).explosionResistance(2.0f)))
            .prefix("stripped")
            .suffix("wood")
            .commonPostInit(LOG_FLAMMMABLE)
            .build();
    public static final BlockVariant<WoodSet> STRIPPED_LOG = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(barkColors(set).explosionResistance(2.0f)))
            .prefix("stripped")
            .suffix("log")
            .commonPostInit(LOG_FLAMMMABLE)
            .build();
    public static final BlockVariant<WoodSet> PLANKS = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(plankColors(set)))
            .suffix("planks")
            .commonPostInit(WOOD_FLAMMABLE)
            .build();

    private WoodVariants() {
    }

    private static BlockBehaviour.Properties axisDependentColors(WoodSet set) {
        return BlockUtils.colorFunction(set.getBaseProperties().get(), blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? set.getWoodColor() : set.getBarkColor());
    }

    private static BlockBehaviour.Properties plankColors(WoodSet set) {
        return set.getBaseProperties().get().color(set.getWoodColor());
    }

    private static BlockBehaviour.Properties barkColors(WoodSet set) {
        return set.getBaseProperties().get().color(set.getWoodColor());
    }
}

package com.teamaurora.magnetosphere.api.block.v1.set.wood;

import com.teamaurora.magnetosphere.api.block.v1.BlockUtils;
import com.teamaurora.magnetosphere.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.magnetosphere.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.magnetosphere.api.content_registries.v1.FlammabilityRegistry;
import com.teamaurora.magnetosphere.api.content_registries.v1.StrippingRegistry;
import com.teamaurora.magnetosphere.api.content_registries.v1.client.RenderTypeRegistry;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.logging.log4j.util.TriConsumer;

public final class WoodVariants {

    private static final TriConsumer<ModLoaderService.ParallelDispatcher, WoodSet, RegistryReference<Block>> LOG_FLAMMMABLE = (dispatcher, woodSet, block) ->
            dispatcher.enqueueWork(() -> FlammabilityRegistry.register(5, 5, block.get()));
    private static final TriConsumer<ModLoaderService.ParallelDispatcher, WoodSet, RegistryReference<Block>> WOOD_FLAMMABLE = (dispatcher, woodSet, block) ->
            dispatcher.enqueueWork(() -> FlammabilityRegistry.register(5, 20, block.get()));

    public static final BlockVariant<WoodSet> STRIPPED_WOOD = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(plankColors(set).explosionResistance(2.0f)))
            .prefix("stripped")
            .suffix("wood")
            .commonPostInit(LOG_FLAMMMABLE)
            .build();
    public static final BlockVariant<WoodSet> STRIPPED_LOG = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(plankColors(set).explosionResistance(2.0f)))
            .prefix("stripped")
            .suffix("log")
            .commonPostInit(LOG_FLAMMMABLE)
            .build();
    public static final BlockVariant<WoodSet> PLANKS = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(plankColors(set)))
            .suffix("planks")
            .commonPostInit(WOOD_FLAMMABLE)
            .build();
    public static final BlockVariant<WoodSet> LOG = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(axisDependentColors(set).explosionResistance(2.0f)))
            .suffix("log")
            .commonPostInit((d, s, o) -> {
                LOG_FLAMMMABLE.accept(d, s, o);
                StrippingRegistry.register(o.get(), s.variantOrThrow(STRIPPED_LOG).get());
            })
            .build();
    public static final BlockVariant<WoodSet> WOOD = BlockVariant.<WoodSet>builder(set ->
                    () -> new RotatedPillarBlock(barkColors(set).explosionResistance(2.0f)))
            .suffix("wood")
            .commonPostInit((d, s, o) -> {
                LOG_FLAMMMABLE.accept(d, s, o);
                StrippingRegistry.register(o.get(), s.variantOrThrow(STRIPPED_WOOD).get());
            })
            .build();
    public static final BlockVariant<WoodSet> SLAB = BlockVariant.<WoodSet>builder(set ->
                    () -> new SlabBlock(plankColors(set)))
            .suffix("slab")
            .commonPostInit(WOOD_FLAMMABLE)
            .build();
    public static final BlockVariant<WoodSet> STAIRS = BlockVariant.<WoodSet>builder(set ->
                    () -> new StairBlock(set.variantOrThrow(PLANKS).get().defaultBlockState(), plankColors(set)))
            .suffix("stairs")
            .commonPostInit(WOOD_FLAMMABLE)
            .build();
    public static final BlockVariant<WoodSet> FENCE = BlockVariant.<WoodSet>builder(set ->
                    () -> new FenceBlock(plankColors(set)))
            .suffix("fence")
            .commonPostInit(WOOD_FLAMMABLE)
            .build();
    public static final BlockVariant<WoodSet> FENCE_GATE = BlockVariant.<WoodSet>builder(set ->
                    () -> new FenceGateBlock(plankColors(set), set.getWoodType()))
            .suffix("fence_gate")
            .commonPostInit(WOOD_FLAMMABLE)
            .build();
    public static final BlockVariant<WoodSet> PRESSURE_PLATE = BlockVariant.<WoodSet>builder(set ->
                    () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, plankColors(set).strength(0.5f).noCollission(), set.getWoodType().setType()))
            .suffix("pressure_plate")
            .build();
    public static final BlockVariant<WoodSet> BUTTON = BlockVariant.<WoodSet>builder(set ->
                    () -> new ButtonBlock(plankColors(set).strength(0.5F).noCollission(), set.getWoodType().setType(), 30, true))
            .suffix("button")
            .build();
    public static final BlockVariant<WoodSet> TRAPDOOR = BlockVariant.<WoodSet>builder(set ->
                    () -> new TrapDoorBlock(plankColors(set).strength(3F).noOcclusion(), set.getWoodType().setType()))
            .suffix("trapdoor")
            .clientPostInit(() -> (d, s, o) -> {
                d.enqueueWork(() -> RenderTypeRegistry.register(RenderType.cutout(), o.get()));
            })
            .build();
    public static final BlockVariant<WoodSet> DOOR = BlockVariant.<WoodSet>builder(set ->
                    () -> new DoorBlock(plankColors(set).strength(3F).noOcclusion(), set.getWoodType().setType()))
            .suffix("door")
            .clientPostInit(() -> (d, s, o) -> {
                d.enqueueWork(() -> RenderTypeRegistry.register(RenderType.cutout(), o.get()));
            })
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

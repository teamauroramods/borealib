package com.teamaurora.borealib.api.block.v1.compat.forge;

import com.teamaurora.borealib.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;

public final class ForgeCompatBlockVariants {

    public static final BlockVariant<WoodSet> STRIPPED_POST = BlockVariant.<WoodSet>builder(set ->
                    () -> new BorealibPostBlock(WoodVariants.plankColors(set)))
            .prefix("stripped")
            .suffix("post")
            .build();
    public static final BlockVariant<WoodSet> POST = BlockVariant.<WoodSet>builder(set ->
                    () -> new BorealibPostBlock(set.variantOrThrow(STRIPPED_POST), WoodVariants.plankColors(set)))
            .suffix("post")
            .build();
    public static final BlockVariant<WoodSet> VERTICAL_PLANKS = BlockVariant.<WoodSet>builder(set ->
                    () -> new Block(WoodVariants.plankColors(set)))
            .prefix("vertical")
            .suffix("planks")
            .build();
    public static final BlockVariant<WoodSet> WOODEN_VERTICAL_SLAB = BlockVariant.<WoodSet>builder(set ->
                    () -> new BorealibVerticalSlabBlock(WoodVariants.plankColors(set)))
            .suffix("vertical_slab")
            .build();
    public static final BlockVariant<WoodSet> HEDGE = BlockVariant.<WoodSet>builder(set ->
                    () -> new BorealibHedgeBlock(WoodVariants.axisDependentColors(set).explosionResistance(2.0f)))
            .suffix("hedge")
            .build();

    private ForgeCompatBlockVariants() {
    }
}

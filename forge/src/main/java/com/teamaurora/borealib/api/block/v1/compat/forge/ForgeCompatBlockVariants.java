package com.teamaurora.borealib.api.block.v1.compat.forge;

import com.teamaurora.borealib.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;

import static com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants.plankColors;

public final class ForgeCompatBlockVariants {

    public static final BlockVariant<WoodSet> STRIPPED_POST = BlockVariant.<WoodSet>builder(set ->
                    () -> new BorealibPostBlock(plankColors(set)))
            .prefix("stripped")
            .suffix("post")
            .build();
    public static final BlockVariant<WoodSet> POST = BlockVariant.<WoodSet>builder(set ->
                    () -> new BorealibPostBlock(set.variantOrThrow(STRIPPED_POST), plankColors(set)))
            .suffix("post")
            .build();
    public static final BlockVariant<WoodSet> VERTICAL_PLANKS = BlockVariant.<WoodSet>builder(set ->
                    () -> new Block(plankColors(set)))
            .prefix("vertical")
            .suffix("planks")
            .build();
    public static final BlockVariant<WoodSet> WOODEN_VERTICAL_SLAB = BlockVariant.<WoodSet>builder(set ->
                    () -> new BorealibVerticalSlabBlock(plankColors(set)))
            .suffix("vertical_slab")
            .build();

    private ForgeCompatBlockVariants() {
    }
}

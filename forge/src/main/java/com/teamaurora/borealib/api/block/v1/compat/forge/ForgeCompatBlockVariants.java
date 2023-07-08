package com.teamaurora.borealib.api.block.v1.compat.forge;

import com.teamaurora.borealib.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import com.teamaurora.borealib.api.content_registries.v1.client.render.RenderTypeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

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
            .clientPostInit(() -> (dispatch, set, block) -> RenderTypeRegistry.register(RenderType.cutoutMipped(), block.get()))
            .build();
    public static final BlockVariant<WoodSet> LADDER = BlockVariant.<WoodSet>builder(set ->
                    () -> new LadderBlock(BlockBehaviour.Properties.of().forceSolidOff().strength(0.4F).sound(SoundType.LADDER).noOcclusion().pushReaction(PushReaction.DESTROY)))
            .suffix("ladder")
            .clientPostInit(() -> (dispatch, set, block) -> RenderTypeRegistry.register(RenderType.cutout(), block.get()))
            .build();

    private ForgeCompatBlockVariants() {
    }
}

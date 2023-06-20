package com.teamaurora.borealib.api.block.v1.compat;

import com.teamaurora.borealib.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public final class CommonCompatBlockVariants {

    // For Carpenter, Woodworks, Quark
    public static final BlockVariant<WoodSet> WOODEN_CHEST = BlockVariant.<WoodSet>builder(set ->
                () -> new BorealibChestBlock(new ResourceLocation(set.getNamespace(), set.getBaseName()), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava()))
            .suffix("chest")
            .build();

    // For Carpenter, Woodworks, Quark
    public static final BlockVariant<WoodSet> WOODEN_TRAPPED_CHEST = BlockVariant.<WoodSet>builder(set ->
                    () -> new BorealibTrappedChestBlock(new ResourceLocation(set.getNamespace(), set.getBaseName() + "_trapped"), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava()))
            .suffix("trapped_chest")
            .build();

    private CommonCompatBlockVariants() {
    }
}

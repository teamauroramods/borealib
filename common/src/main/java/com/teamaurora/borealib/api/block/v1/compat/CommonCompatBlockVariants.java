package com.teamaurora.borealib.api.block.v1.compat;

import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibChestBlockEntity;
import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibTrappedChestBlockEntity;
import com.teamaurora.borealib.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.item.v1.BEWLRBlockItem;
import com.teamaurora.borealib.core.client.render.block.entity.ChestBlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

/**
 * Default compatibility block variants for use with {@link WoodSet}s.
 *
 * @author ebo2022
 * @since 1.0
 */
public final class CommonCompatBlockVariants {

    // For Carpenter, Woodworks, Quark
    public static final BlockVariant<WoodSet> WOODEN_CHEST = BlockVariant.<WoodSet>builder(set ->
                () -> new BorealibChestBlock(new ResourceLocation(set.getNamespace(), set.getBaseName()), BlockBehaviour.Properties.of().mapColor(set.getWoodColor()).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(set.getWoodType().soundType()).ignitedByLava()))
            .suffix("chest")
            .blockItemFactory((set, block) -> new BEWLRBlockItem(block, new Item.Properties(), () -> () -> chestBEWLR(false)))
            .build();
    // For Carpenter, Woodworks, Quark
    public static final BlockVariant<WoodSet> WOODEN_TRAPPED_CHEST = BlockVariant.<WoodSet>builder(set ->
                    () -> new BorealibTrappedChestBlock(new ResourceLocation(set.getNamespace(), set.getBaseName() + "_trapped"), BlockBehaviour.Properties.of().mapColor(set.getWoodColor()).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(set.getWoodType().soundType()).ignitedByLava()))
            .suffix("trapped_chest")
            .blockItemFactory((set, block) -> new BEWLRBlockItem(block, new Item.Properties(), () -> () -> chestBEWLR(true)))
            .build();
    public static final BlockVariant<WoodSet> BOOKSHELF = BlockVariant.<WoodSet>builder(set ->
                    () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(1.5F).sound(SoundType.WOOD).ignitedByLava()))
            .suffix("bookshelf")
            .build();

    private CommonCompatBlockVariants() {
    }

    private static BEWLRBlockItem.LazyBEWLR chestBEWLR(boolean trapped) {
        return trapped ? new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> new ChestBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new BorealibTrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState()))) : new BEWLRBlockItem.LazyBEWLR((dispatcher, entityModelSet) -> new ChestBlockEntityWithoutLevelRenderer<>(dispatcher, entityModelSet, new BorealibChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState())));
    }
}

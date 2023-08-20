package com.teamaurora.borealib.api.registry.v1.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class PropertiesHelper {

    public static Item.Properties stacksOnce() {
        return new Item.Properties().stacksTo(1);
    }

    public static Item.Properties food(FoodProperties food) {
        return new Item.Properties().food(food);
    }

    public static BlockBehaviour.Properties flower() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(Block.OffsetType.XZ);
    }

    public static BlockBehaviour.Properties sapling() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS);
    }

    public static BlockBehaviour.Properties ladder() {
        return BlockBehaviour.Properties.of().forceSolidOff().strength(0.4F).sound(SoundType.LADDER).noOcclusion().pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties flowerPot() {
        return BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties thatch(MapColor color, SoundType soundType) {
        return BlockBehaviour.Properties.of().mapColor(color).strength(0.5F).sound(soundType).noOcclusion();
    }

    public static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
        return false;
    }

    public static boolean never(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> entity) {
        return false;
    }

    public static boolean always(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    public static boolean always(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> entity) {
        return true;
    }

    public static boolean ocelotOrParrot(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }
}

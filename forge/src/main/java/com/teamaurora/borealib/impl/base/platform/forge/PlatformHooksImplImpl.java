package com.teamaurora.borealib.impl.base.platform.forge;

import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.*;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;

public class PlatformHooksImplImpl {

    public static boolean isMobGriefingOn(Level level, Entity entity) {
        return ForgeEventFactory.getMobGriefingEvent(level, entity);
    }

    public static boolean isAreaLoaded(LevelReader level, BlockPos pos, int maxRange) {
        return level.isAreaLoaded(pos, maxRange);
    }

    public static int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return state.getFlammability(level, pos, face);
    }

    public static int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return state.getFireSpreadSpeed(level, pos, face);
    }

    @Nullable
    public static FoodProperties getFoodProperties(Item food, ItemStack stack, Player player) {
        return food.getFoodProperties(stack, player);
    }

    public static int getBurnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null);
    }

    public static boolean onProjectileImpact(Projectile improvedProjectileEntity, HitResult blockHitResult) {
        return ForgeEventFactory.onProjectileImpact(improvedProjectileEntity, blockHitResult);
    }

    public static boolean isCurativeItem(ItemStack stack, MobEffectInstance effect) {
        return effect.isCurativeItem(stack);
    }

    public static boolean canHarvestBlock(BlockState state, ServerLevel level, BlockPos pos, ServerPlayer player) {
        return state.canHarvestBlock(level, pos, player);
    }

    public static float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return state.getFriction(level, pos, entity);
    }

    public static boolean canEquipItem(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        return stack.canEquip(slot, entity);
    }

    public static boolean canEntityDestroy(Level level, BlockPos blockPos, Animal animal) {
        return ForgeHooks.canEntityDestroy(level, blockPos, animal);
    }

    public static boolean onExplosionStart(Level level, Explosion explosion) {
        return ForgeEventFactory.onExplosionStart(level, explosion);
    }

    public static void onLivingConvert(LivingEntity frFom, LivingEntity to) {
        ForgeEventFactory.onLivingConvert(frFom, to);
    }

    public static boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, IntConsumer timer) {
        return ForgeEventFactory.canLivingConvert(entity, outcome, timer::accept);
    }

    public static void onExplosionDetonate(Level level, Explosion explosion, List<Entity> entities, double diameter) {
        ForgeEventFactory.onExplosionDetonate(level, explosion, entities, diameter);
    }

    public static double getReachDistance(LivingEntity entity) {
        return entity.getAttribute(ForgeMod.BLOCK_REACH.get()).getValue();
    }

    public static float getExplosionResistance(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        return state.getExplosionResistance(level, pos, explosion);
    }

    public static void onBlockExploded(BlockState blockstate, Level level, BlockPos blockpos, Explosion explosion) {
        blockstate.onBlockExploded(level, blockpos, explosion);
    }

    public static boolean areStacksEqual(ItemStack stack, ItemStack other, boolean sameNbt) {
        return stack.equals(other, sameNbt);
    }

    public static boolean isFireSource(BlockState blockState, Level level, BlockPos pos, Direction up) {
        return blockState.isFireSource(level, pos, up);
    }

    public static boolean canDropFromExplosion(BlockState blockstate, Level level, BlockPos blockpos, Explosion explosion) {
        return blockstate.canDropFromExplosion(level, blockpos, explosion);
    }

    public static boolean isDye(ItemStack itemstack) {
        return itemstack.is(Tags.Items.DYES);
    }

    @Nullable
    public static DyeColor getColor(ItemStack stack) {
        return DyeColor.getColor(stack);
    }

    public static BlockState rotateBlock(BlockState state, Level world, BlockPos targetPos, Rotation rot) {
        return state.rotate(world, targetPos, rot);
    }

    public static boolean isMultipartEntity(Entity e) {
        return e.isMultipartEntity();
    }

    public static RailShape getRailDirection(BaseRailBlock railBlock, BlockState blockstate, Level level, BlockPos blockpos, @Nullable AbstractMinecart o) {
        return railBlock.getRailDirection(blockstate, level, blockpos, o);
    }

    public static Optional<ItemStack> getCraftingRemainingItem(ItemStack itemstack) {
        return itemstack.hasCraftingRemainingItem() ? Optional.of(itemstack.getCraftingRemainingItem()) : Optional.empty();
    }

    public static void reviveEntity(Entity entity) {
        entity.revive();
    }

    public static boolean onCropsGrowPre(ServerLevel level, BlockPos pos, BlockState state, boolean b) {
        return ForgeHooks.onCropsGrowPre(level, pos, state, b);
    }

    public static void onCropsGrowPost(ServerLevel level, BlockPos pos, BlockState state) {
        ForgeHooks.onCropsGrowPost(level, pos, state);
    }

    public static void onEquipmentChange(LivingEntity entity, EquipmentSlot slot, ItemStack from, ItemStack to) {
        MinecraftForge.EVENT_BUS.post(new LivingEquipmentChangeEvent(entity, slot, from, to));
    }

    public static InteractionResult onRightClickBlock(Player player, InteractionHand hand, BlockPos below, BlockHitResult rayTraceResult) {
        return ForgeHooks.onRightClickBlock(player, hand, below, rayTraceResult).getCancellationResult();
    }

    public static boolean canItemStack(ItemStack selected, ItemStack item) {
        return ItemHandlerHelper.canItemStacksStack(selected, item);
    }

    public static boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, Block block) {
        if (!(block instanceof IPlantable plantable)) throw new IllegalArgumentException("Block " + RegistryWrapper.BLOCK.getKey(block) + " is not IPlantable");
        return state.canSustainPlant(level, pos, facing, plantable);
    }
}

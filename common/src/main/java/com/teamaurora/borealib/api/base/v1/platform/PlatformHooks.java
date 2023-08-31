package com.teamaurora.borealib.api.base.v1.platform;

import com.teamaurora.borealib.impl.base.platform.PlatformHooksImpl;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;

/**
 * Wraps Forge's context-based methods to work where possible with Fabric.
 *
 * @see <a href=https://github.com/MinecraftForge/MinecraftForge>Minecraft Forge</a>
 * @author ebo2022
 * @since 1.0
 */
public interface PlatformHooks {

    static boolean isMobGriefingOn(Level level, Entity entity) {
        return PlatformHooksImpl.isMobGriefingOn(level, entity);
    }

    static boolean isAreaLoaded(LevelReader level, BlockPos pos, int maxRange) {
        return PlatformHooksImpl.isAreaLoaded(level, pos, maxRange);
    }

    static int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return PlatformHooksImpl.getFlammability(state, level, pos, face);
    }

    static int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return PlatformHooksImpl.getFireSpreadSpeed(state, level, pos, face);
    }

    @Nullable
    static FoodProperties getFoodProperties(Item food, ItemStack stack, Player player) {
        return PlatformHooksImpl.getFoodProperties(food, stack, player);
    }

    static int getBurnTime(ItemStack stack) {
        return PlatformHooksImpl.getBurnTime(stack);
    }

    static boolean onProjectileImpact(Projectile improvedProjectileEntity, HitResult blockHitResult) {
        return PlatformHooksImpl.onProjectileImpact(improvedProjectileEntity, blockHitResult);
    }

    static boolean isCurativeItem(ItemStack stack, MobEffectInstance effect) {
        return PlatformHooksImpl.isCurativeItem(stack, effect);
    }

    static boolean canHarvestBlock(BlockState state, ServerLevel level, BlockPos pos, ServerPlayer player) {
        return PlatformHooksImpl.canHarvestBlock(state, level, pos, player);
    }

    static float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return PlatformHooksImpl.getFriction(state, level, pos, entity);
    }

    static boolean canEquipItem(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        return PlatformHooksImpl.canEquipItem(entity, stack, slot);
    }

    static boolean canEntityDestroy(Level level, BlockPos blockPos, Animal animal) {
        return PlatformHooksImpl.canEntityDestroy(level, blockPos, animal);
    }

    static boolean onExplosionStart(Level level, Explosion explosion) {
        return PlatformHooksImpl.onExplosionStart(level, explosion);
    }

    static void onLivingConvert(LivingEntity frFom, LivingEntity to) {
        PlatformHooksImpl.onLivingConvert(frFom, to);
    }

    static boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, IntConsumer timer) {
        return PlatformHooksImpl.canLivingConvert(entity, outcome, timer);
    }

    static void onExplosionDetonate(Level level, Explosion explosion, List<Entity> entities, double diameter) {
        PlatformHooksImpl.onExplosionDetonate(level, explosion, entities, diameter);
    }

    static double getReachDistance(LivingEntity entity) {
        return PlatformHooksImpl.getReachDistance(entity);
    }

    static float getExplosionResistance(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        return PlatformHooksImpl.getExplosionResistance(state, level, pos, explosion);
    }

    static void onBlockExploded(BlockState blockstate, Level level, BlockPos blockpos, Explosion explosion) {
        PlatformHooksImpl.onBlockExploded(blockstate, level, blockpos, explosion);
    }

    static boolean areStacksEqual(ItemStack stack, ItemStack other, boolean sameNbt) {
        return PlatformHooksImpl.areStacksEqual(stack, other, sameNbt);
    }

    static boolean isFireSource(BlockState blockState, Level level, BlockPos pos, Direction up) {
        return PlatformHooksImpl.isFireSource(blockState, level, pos, up);
    }

    static boolean canDropFromExplosion(BlockState blockstate, Level level, BlockPos blockpos, Explosion explosion) {
        return PlatformHooksImpl.canDropFromExplosion(blockstate, level, blockpos, explosion);
    }

    static boolean isDye(ItemStack itemstack) {
        return PlatformHooksImpl.isDye(itemstack);
    }

    @Nullable
    static DyeColor getColor(ItemStack stack) {
        return PlatformHooksImpl.getColor(stack);
    }

    static BlockState rotateBlock(BlockState state, Level world, BlockPos targetPos, Rotation rot) {
        return PlatformHooksImpl.rotateBlock(state, world, targetPos, rot);
    }

    static boolean isMultipartEntity(Entity e) {
        return PlatformHooksImpl.isMultipartEntity(e);
    }

    static RailShape getRailDirection(BaseRailBlock railBlock, BlockState blockstate, Level level, BlockPos blockpos, @Nullable AbstractMinecart o) {
        return PlatformHooksImpl.getRailDirection(railBlock, blockstate, level, blockpos, o);
    }

    static Optional<ItemStack> getCraftingRemainingItem(ItemStack itemstack) {
        return PlatformHooksImpl.getCraftingRemainingItem(itemstack);
    }

    static void reviveEntity(Entity entity) {
        PlatformHooksImpl.reviveEntity(entity);
    }

    static boolean onCropsGrowPre(ServerLevel level, BlockPos pos, BlockState state, boolean b) {
        return PlatformHooksImpl.onCropsGrowPre(level, pos, state, b);
    }

    static void onCropsGrowPost(ServerLevel level, BlockPos pos, BlockState state) {
        PlatformHooksImpl.onCropsGrowPost(level, pos, state);
    }

    static void onEquipmentChange(LivingEntity entity, EquipmentSlot slot, ItemStack from, ItemStack to) {
        PlatformHooksImpl.onEquipmentChange(entity, slot, from, to);
    }

    static InteractionResult onRightClickBlock(Player player, InteractionHand hand, BlockPos below, BlockHitResult rayTraceResult) {
        return PlatformHooksImpl.onRightClickBlock(player, hand, below, rayTraceResult);
    }

    static boolean canItemStack(ItemStack selected, ItemStack item) {
        return PlatformHooksImpl.canItemStack(selected, item);
    }

    static boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, Block block) {
        return PlatformHooksImpl.canSustainPlant(state, level, pos, facing, block);
    }
}

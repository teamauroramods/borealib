package com.teamaurora.borealib.impl.base.platform;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;

@ApiStatus.Internal
public class PlatformHooksImpl {

    @ExpectPlatform
    public static boolean isMobGriefingOn(Level level, Entity entity) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean isAreaLoaded(LevelReader level, BlockPos pos, int maxRange) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return Platform.expect();
    }

    @ExpectPlatform
    @Nullable
    public static FoodProperties getFoodProperties(Item food, ItemStack stack, Player player) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static int getBurnTime(ItemStack stack) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean onProjectileImpact(Projectile improvedProjectileEntity, HitResult blockHitResult) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean isCurativeItem(ItemStack stack, MobEffectInstance effect) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean canHarvestBlock(BlockState state, ServerLevel level, BlockPos pos, ServerPlayer player) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean canEquipItem(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean canEntityDestroy(Level level, BlockPos blockPos, Animal animal) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean onExplosionStart(Level level, Explosion explosion) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static void onLivingConvert(LivingEntity frFom, LivingEntity to) {
        Platform.expect();
    }

    @ExpectPlatform
    public static boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, IntConsumer timer) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static void onExplosionDetonate(Level level, Explosion explosion, List<Entity> entities, double diameter) {
        Platform.expect();
    }

    @ExpectPlatform
    public static double getReachDistance(LivingEntity entity) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static float getExplosionResistance(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static void onBlockExploded(BlockState blockstate, Level level, BlockPos blockpos, Explosion explosion) {
        Platform.expect();
    }

    @ExpectPlatform
    public static boolean areStacksEqual(ItemStack stack, ItemStack other, boolean sameNbt) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean isFireSource(BlockState blockState, Level level, BlockPos pos, Direction up) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean canDropFromExplosion(BlockState blockstate, Level level, BlockPos blockpos, Explosion explosion) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean isDye(ItemStack itemstack) {
        return Platform.expect();
    }

    @Nullable
    @ExpectPlatform
    public static DyeColor getColor(ItemStack stack) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static BlockState rotateBlock(BlockState state, Level world, BlockPos targetPos, Rotation rot) {
        return Platform.expect();
    }


    @ExpectPlatform
    public static boolean isMultipartEntity(Entity e) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static RailShape getRailDirection(BaseRailBlock railBlock, BlockState blockstate, Level level, BlockPos blockpos, @Nullable AbstractMinecart o) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static Optional<ItemStack> getCraftingRemainingItem(ItemStack itemstack) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static void reviveEntity(Entity entity) {
        Platform.expect();
    }

    @ExpectPlatform
    public static boolean onCropsGrowPre(ServerLevel level, BlockPos pos, BlockState state, boolean b) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static void onCropsGrowPost(ServerLevel level, BlockPos pos, BlockState state) {
    }

    @ExpectPlatform
    public static void onEquipmentChange(LivingEntity entity, EquipmentSlot slot, ItemStack from, ItemStack to){
    }

    @ExpectPlatform
    public static InteractionResult onRightClickBlock(Player player, InteractionHand hand, BlockPos below, BlockHitResult rayTraceResult) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean canItemStack(ItemStack selected, ItemStack item) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, Block block) {
        return Platform.expect();
    }
}

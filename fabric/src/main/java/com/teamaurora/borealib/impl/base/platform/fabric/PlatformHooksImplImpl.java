package com.teamaurora.borealib.impl.base.platform.fabric;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntConsumer;

@ApiStatus.Internal
public class PlatformHooksImplImpl {

    public static boolean isMobGriefingOn(Level level, Entity entity) {
        return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }

    public static boolean isAreaLoaded(LevelReader level, BlockPos pos, int maxRange) {
        return level.hasChunksAt(pos.offset(-maxRange, -maxRange, -maxRange), pos.offset(maxRange, maxRange, maxRange));
    }

    public static int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return FlammableBlockRegistry.getDefaultInstance().get(state.getBlock()).getBurnChance();
    }

    public static int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return FlammableBlockRegistry.getDefaultInstance().get(state.getBlock()).getSpreadChance();
    }

    @Nullable
    public static FoodProperties getFoodProperties(Item food, ItemStack stack, Player player) {
        return food.getFoodProperties();
    }

    public static int getBurnTime(ItemStack stack) {
        return Objects.requireNonNullElse(FuelRegistry.INSTANCE.get(stack.getItem()), 0);
    }

    public static boolean onProjectileImpact(Projectile improvedProjectileEntity, HitResult blockHitResult) {
        return false;
    }

    public static boolean isCurativeItem(ItemStack stack, MobEffectInstance effect) {
        return stack.getItem() == Items.MILK_BUCKET || stack.getItem() == Items.HONEY_BOTTLE;
    }

    public static boolean canHarvestBlock(BlockState state, ServerLevel level, BlockPos pos, ServerPlayer player) {
        return !state.requiresCorrectToolForDrops() || player.hasCorrectToolForDrops(state);
    }

    public static float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return state.getBlock().getFriction();
    }

    public static boolean canEquipItem(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        return slot == Mob.getEquipmentSlotForItem(stack);
    }

    public static boolean canEntityDestroy(Level level, BlockPos pos, Animal animal) {
        if (!level.isLoaded(pos)) {
            return false;
        } else {
            return isMobGriefingOn(level, animal);
        }
    }

    public static boolean onExplosionStart(Level level, Explosion explosion) {
        return false;
    }

    public static void onLivingConvert(LivingEntity frFom, LivingEntity to) {
    }

    public static boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, IntConsumer timer) {
        return true;
    }

    public static void onExplosionDetonate(Level level, Explosion explosion, List<Entity> entities, double diameter) {
    }

    public static double getReachDistance(LivingEntity entity) {
        return 4.5D;
    }

    public static float getExplosionResistance(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        return state.getBlock().getExplosionResistance();
    }

    public static void onBlockExploded(BlockState blockstate, Level level, BlockPos blockpos, Explosion explosion) {
        level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);
        blockstate.getBlock().wasExploded(level, blockpos, explosion);
    }

    public static boolean areStacksEqual(ItemStack stack, ItemStack other, boolean sameNbt) {
        return stack.equals(other);
    }

    public static boolean isFireSource(BlockState blockState, Level level, BlockPos pos, Direction up) {
        return blockState.is(level.dimensionType().infiniburn());
    }

    public static boolean canDropFromExplosion(BlockState blockstate, Level level, BlockPos blockpos, Explosion explosion) {
        return blockstate.getBlock().dropFromExplosion(explosion);
    }

    public static boolean isDye(ItemStack itemstack) {
        return itemstack.getItem() instanceof DyeItem;
    }

    @Nullable
    public static DyeColor getColor(ItemStack stack) {
        if (stack.getItem() instanceof DyeItem dyeItem) {
            return dyeItem.getDyeColor();
        }
        return null;
    }

    public static BlockState rotateBlock(BlockState state, Level world, BlockPos targetPos, Rotation rot) {
        return state.rotate(rot);
    }

    public static boolean isMultipartEntity(Entity e) {
        return e instanceof EnderDragon;
    }

    public static RailShape getRailDirection(BaseRailBlock railBlock, BlockState blockstate, Level level, BlockPos blockpos, @Nullable AbstractMinecart o) {
        return blockstate.getValue(railBlock.getShapeProperty());
    }

    public static Optional<ItemStack> getCraftingRemainingItem(ItemStack itemstack) {
        return Optional.ofNullable(itemstack.getItem().getCraftingRemainingItem()).map(Item::getDefaultInstance);
    }

    public static void reviveEntity(Entity entity) {
    }

    public static boolean onCropsGrowPre(ServerLevel level, BlockPos pos, BlockState state, boolean b) {
        return b;
    }

    public static void onCropsGrowPost(ServerLevel level, BlockPos pos, BlockState state) {
    }

    public static void onEquipmentChange(LivingEntity entity, EquipmentSlot slot, ItemStack from, ItemStack to) {
    }

    @Nullable
    public static InteractionResult onRightClickBlock(Player player, InteractionHand hand, BlockPos below, BlockHitResult rayTraceResult) {
        return UseBlockCallback.EVENT.invoker().interact(player, player.level(), hand, rayTraceResult);
    }

    public static boolean canItemStack(ItemStack selected, ItemStack item) {
        if (!selected.isEmpty() && ItemStack.isSameItem(selected, item) && selected.hasTag() == item.hasTag()) {
            return (!selected.hasTag() || selected.getTag().equals(item.getTag()));
        } else {
            return false;
        }
    }

    public static boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, Block block) {
        return state.is(BlockTags.DIRT);
    }
}

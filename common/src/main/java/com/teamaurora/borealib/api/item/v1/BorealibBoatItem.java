package com.teamaurora.borealib.api.item.v1;

import com.teamaurora.borealib.api.entity.v1.BorealibBoat;
import com.teamaurora.borealib.api.entity.v1.BorealibBoatType;
import com.teamaurora.borealib.api.entity.v1.BorealibChestBoat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * A boat item that supports spawning custom {@link BorealibBoatType}.
 *
 * @author Ocelot
 * @since 1.4.0
 */
public class BorealibBoatItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    private static final Map<BorealibBoatType, Item> BOAT_ITEMS = new ConcurrentHashMap<>();
    private static final Map<BorealibBoatType, Item> CHEST_BOAT_ITEMS = new ConcurrentHashMap<>();
    private static final DispenseItemBehavior DISPENSE_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource source, ItemStack stack) {
            Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
            Level level = source.getLevel();
            double x = source.x() + direction.getStepX() * 1.125F;
            double y = source.y() + direction.getStepY() * 1.125F;
            double z = source.z() + direction.getStepZ() * 1.125F;
            BlockPos blockPos = source.getPos().relative(direction);
            double offset;
            if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
                offset = 1.0;
            } else {
                if (!level.getBlockState(blockPos).isAir() || !level.getFluidState(blockPos.below()).is(FluidTags.WATER)) {
                    return this.defaultDispenseItemBehavior.dispense(source, stack);
                }
                offset = 0.0;
            }

            BorealibBoatItem item = ((BorealibBoatItem) stack.getItem());
            BorealibBoat boat = item.chest ? new BorealibChestBoat(level, x, y + offset, z) : new BorealibBoat(level, x, y + offset, z);
            boat.setCustomType(item.getType());
            boat.setYRot(direction.toYRot());
            level.addFreshEntity(boat);
            stack.shrink(1);
            return stack;
        }
    };

    private final BorealibBoatType type;
    private final boolean chest;

    public BorealibBoatItem(BorealibBoatType type, boolean chest, Item.Properties properties) {
        super(properties);
        this.type = type;
        this.chest = chest;
        if (this.chest) {
            CHEST_BOAT_ITEMS.put(type, this);
        } else {
            BOAT_ITEMS.put(type, this);
        }
        DispenserBlock.registerBehavior(this, DISPENSE_BEHAVIOR);
    }

    @Nullable
    public static Item getBoatItem(BorealibBoatType type, boolean chest) {
        return chest ? CHEST_BOAT_ITEMS.get(type) : BOAT_ITEMS.get(type);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        HitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hitResult.getType() != HitResult.Type.MISS) {
            Vec3 vec3 = player.getViewVector(1.0F);

            List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
            if (!list.isEmpty()) {
                Vec3 eyePos = player.getEyePosition(1.0F);

                for (Entity entity : list) {
                    AABB box = entity.getBoundingBox().inflate(entity.getPickRadius());
                    if (box.contains(eyePos))
                        return InteractionResultHolder.pass(itemStack);
                }
            }

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BorealibBoat boat = this.getBoat(level, hitResult);
                boat.setCustomType(this.type);
                boat.setYRot(player.getYRot());
                if (!level.noCollision(boat, boat.getBoundingBox().inflate(-0.1)))
                    return InteractionResultHolder.fail(itemStack);

                if (!level.isClientSide()) {
                    level.addFreshEntity(boat);
                    if (!player.isCreative())
                        itemStack.shrink(1);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
            }
        }

        return InteractionResultHolder.pass(itemStack);
    }

    public BorealibBoatType getType() {
        return this.type;
    }

    private BorealibBoat getBoat(Level level, HitResult hitResult) {
        return this.chest
                ? new BorealibChestBoat(level, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z)
                : new BorealibBoat(level, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
    }
}
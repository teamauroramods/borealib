package com.teamaurora.borealib.api.event.entity.v1.player;

import com.teamaurora.borealib.api.base.v1.event.Event;
import gg.moonflower.pollen.api.event.PollinatedEvent;
import gg.moonflower.pollen.api.registry.EventRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public final class PlayerInteractionEvents {

    public static final Event<RightClickItem> RIGHT_CLICK_ITEM = Event.create(RightClickItem.class, events -> (player, level, hand) -> {
        for (RightClickItem event : events) {
            InteractionResultHolder<ItemStack> result = event.interaction(player, level, hand);
            if (result.getResult() != InteractionResult.PASS)
                return result;
        }
        return InteractionResultHolder.pass(ItemStack.EMPTY);
    });

    public static final Event<RightClickEntity> RIGHT_CLICK_ENTITY = Event.createResult(RightClickEntity.class);
    public static final Event<RightClickBlock> RIGHT_CLICK_BLOCK = Event.createResult(RightClickBlock.class);
    public static final Event<LeftClickBlock> LEFT_CLICK_BLOCK = Event.createResult(LeftClickBlock.class);

    private PlayerInteractionEvents() {
    }

    /**
     * Ran when a player right-clicks any item.
     *
     * @since 1.0
     */
    @FunctionalInterface
    public interface RightClickItem {
        InteractionResultHolder<ItemStack> interaction(Player player, Level level, InteractionHand hand);
    }

    /**
     * Fired when a player is right-clicking an entity.
     *
     * @since 1.0
     */
    @FunctionalInterface
    public interface RightClickEntity {
        InteractionResult interaction(Player player, Level level, InteractionHand hand, Entity entity);
    }

    /**
     * Fired when a player right-clicks an item while looking at a block.
     *
     * @since 1.0
     */
    @FunctionalInterface
    public interface RightClickBlock {
        InteractionResult interaction(Player player, Level level, InteractionHand hand, BlockHitResult hitResult);
    }

    /**
     * Fired when a player left-clicks a block.
     *
     * @since 1.0
     */
    @FunctionalInterface
    public interface LeftClickBlock {
        InteractionResult interaction(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction);
    }
}
package com.teamaurora.borealib.api.event.entity.v1.player;

import com.teamaurora.borealib.api.base.v1.event.Event;
import com.teamaurora.borealib.api.base.v1.util.MutableInt;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class PlayerEvents {

    public static final Event<PlayerAdvancementEvent> ADVANCEMENT = Event.createLoop(PlayerAdvancementEvent.class);
    public static final Event<ExpPickup> EXP_PICKUP = Event.createCancellable(ExpPickup.class);
    public static final Event<ExpChange> EXP_CHANGE = Event.createCancellable(ExpChange.class);
    public static final Event<LevelChange> LEVEL_CHANGE = Event.createCancellable(LevelChange.class);
    public static final Event<StartSleeping> START_SLEEPING = Event.create(StartSleeping.class, events -> (player, pos) -> {
        for (StartSleeping event : events) {
            Player.BedSleepingProblem result = event.startSleeping(player, pos);
            if (result != null)
                return result;
        }
        return null;
    });
    public static final Event<StopSleeping> STOP_SLEEPING = Event.createLoop(StopSleeping.class);
    public static final Event<Respawn> RESPAWN = Event.createLoop(Respawn.class);
    public static final Event<ItemCrafted> ITEM_CRAFTED = Event.createLoop(ItemCrafted.class);
    public static final Event<ItemSmelted> ITEM_SMELTED = Event.createLoop(ItemSmelted.class);

    private PlayerEvents() {
    }

    /**
     * Fired when a player is awarded an advancement.
     *
     * @author ebo2022
     * @since
     */
    @FunctionalInterface
    public interface PlayerAdvancementEvent {

        /**
         * Called when the specified player is awarded the given advancement.
         *
         * @param player      The player being awarded the advancement
         * @param advancement The advancement being awarded
         */
        void playerAdvancement(Player player, Advancement advancement);
    }

    /**
     * Fired when a player picks up experience orbs.
     *
     * @author ebo2022
     * @since
     */
    @FunctionalInterface
    public interface ExpPickup {

        /**
         * Called after the specified player collides with the specified orb, before the player is awarded the EXP.
         *
         * @param player The player that collided with the specified orb
         * @param orb    The {@link ExperienceOrb} being picked up by the player
         * @return <code>true</code> to continue the process, or <code>false</code> to cancel it
         */
        boolean expPickup(Player player, ExperienceOrb orb);
    }

    /**
     * Fired when a player is about to be awarded experience.
     *
     * @author ebo2022
     * @since
     */
    @FunctionalInterface
    public interface ExpChange {

        /**
         * Called before the specified player is given experience.
         *
         * @param player The player being given experience
         * @param xp     The amount of experience to give to the player. This can be modified by event listeners
         * @return <code>true</code> to continue, or <code>false</code> to stop further processing
         */
        boolean expChange(Player player, MutableInt xp);
    }

    /**
     * Fired when a player is about to be awarded experience levels.
     *
     * @author ebo2022
     * @since
     */
    @FunctionalInterface
    public interface LevelChange {

        /**
         * Called before the specified player is given levels, allowing for the amount to be modified.
         *
         * @param player The player being given experience levels
         * @param levels The amount of levels to give to the player. This can be modified by event listeners
         * @return <code>true</code> to continue, or <code>false</code> to stop further processing
         */
        boolean levelChange(Player player, MutableInt levels);
    }

    /**
     * Fired when a player starts sleeping.
     *
     * @author ebo2022
     * @since
     */
    @FunctionalInterface
    public interface StartSleeping {

        /**
         * Called when the specified player starts sleeping at the given {@link BlockPos}.
         *
         * @param player The player falling asleep
         * @param pos    The sleeping position of the player
         * @return <code>null</code> to allow the player to sleep, or a {@link Player.BedSleepingProblem} if they cannot sleep
         */

        @Nullable
        Player.BedSleepingProblem startSleeping(Player player, BlockPos pos);
    }

    /**
     * Fired when a player stops sleeping and wakes up.
     *
     * @author ebo2022
     * @since
     */
    @FunctionalInterface
    public interface StopSleeping {

        /**
         * Called when the specified player wakes up.
         *
         * @param player The player waking up
         * @param wakeImmediately Whether the player is waking up immediately
         * @param updateLevel Whether the list of sleeping players is being updated
         */
        void stopSleeping(Player player, boolean wakeImmediately, boolean updateLevel);
    }

    /**
     * Fired when a player respawns.
     *
     * @author ebo2022
     * @since
     */
    @FunctionalInterface
    public interface Respawn {

        /**
         * Called when the specified player is about to respawn.
         *
         * @param player       The player respawning
         * @param endConquered Whether the player has won the game and is leaving the end
         */
        void respawn(ServerPlayer player, boolean endConquered);
    }

    /**
     * Fired when a player crafts an item.
     *
     * @author ebo2022
     * @since
     */
    @FunctionalInterface
    public interface ItemCrafted {

        /**
         * Called when the specified player takes the finished item out of the output slot.
         *
         * @param player    The player taking the item
         * @param stack     The item that has been crafteed
         * @param container The crafting table's inventory
         */
        void craft(Player player, ItemStack stack, Container container);
    }

    /**
     * Fired when a player smelts an item.
     *
     * @author ebo2022
     * @since
     */
    @FunctionalInterface
    public interface ItemSmelted {

        /**
         * Called when the specified player takes the given item out of the furnace's output slot.
         *
         * @param player The player taking the smelted  item
         * @param stack  The smelted item
         */
        void smelt(Player player, ItemStack stack);
    }
}
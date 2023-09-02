package com.teamaurora.borealib.api.item.v1;

import com.teamaurora.borealib.api.registry.v1.util.PropertiesHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author rose_
 */
public class DrinkableBottleItem extends HoneyBottleItem {
    private final Supplier<SoundEvent> drinkSound;

    public DrinkableBottleItem(Supplier<SoundEvent> drinkSound, FoodProperties food) {
        super(PropertiesHelper.food(food).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
        this.drinkSound = drinkSound;
    }

    public DrinkableBottleItem(FoodProperties food) {
        this(() -> SoundEvents.GENERIC_DRINK, food);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack bottle = Items.GLASS_BOTTLE.getDefaultInstance();
        entity.eat(level, stack);

        if (entity instanceof ServerPlayer player) {
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        if (stack.isEmpty())
            return bottle;
        else {
            if (entity instanceof Player player && !player.getAbilities().instabuild)
                if (!player.getInventory().add(bottle))
                    player.drop(bottle, false);

            return stack;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return Objects.requireNonNull(this.getFoodProperties()).canAlwaysEat() || player.canEat(false) ? super.use(level, player, hand)
                : InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return this.drinkSound.get();
    }

    @Override
    public SoundEvent getEatingSound() {
        return this.drinkSound.get();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }
}
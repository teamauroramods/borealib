package com.teamaurora.borealib.api.event.entity.v1;

import com.teamaurora.borealib.api.base.v1.event.Event;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;

/**
 * Events for modifying villager-like entity trades.
 *
 * @author ebo2022
 * @since 1.0
 */
public final class TradeEvents {

    public static final Event<ModifyVillager> VILLAGER = Event.createLoop(ModifyVillager.class);
    public static final Event<ModifyWanderer> WANDERER = Event.createLoop(ModifyWanderer.class);

    private TradeEvents() {
    }

    /**
     * Fired to setup any modded villager trades.
     *
     * @since 1.0
     */
    @FunctionalInterface
    public interface ModifyVillager {

        /**
         * Modifies the trades.
         *
         * @param context Context to add custom villager trades
         */
        void modifyTrades(Context context);

        /**
         * The context used to modify the villager trades.
         *
         * @since 1.0
         */
        interface Context {

            VillagerProfession getProfession();

            TradeList getTrades(int tier);

            int getMinTier();

            int getMaxTier();
        }
    }

    /**
     * Fired to setup any modded wanderer trades.
     *
     * @since 1.0
     */
    @FunctionalInterface
    public interface ModifyWanderer {

        /**
         * Modifies the trades.
         *
         * @param context Context to add custom wanderer trades
         */
        void modifyTrades(Context context);

        /**
         * The context used to modify the wanderer trades.
         *
         * @since 1.0
         */
        interface Context {

            TradeList getGeneric();

            TradeList getRare();
        }
    }

    public static class TradeList implements List<VillagerTrades.ItemListing> {

        private final List<VillagerTrades.ItemListing> trades;

        public TradeList() {
            this.trades = NonNullList.create();
        }

        @Override
        public int size() {
            return this.trades.size();
        }

        @Override
        public boolean isEmpty() {
            return this.trades.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return this.trades.contains(o);
        }

        @NotNull
        @Override
        public Iterator<VillagerTrades.ItemListing> iterator() {
            return this.trades.iterator();
        }

        @NotNull
        @Override
        public Object[] toArray() {
            return this.trades.toArray();
        }

        @NotNull
        @Override
        public <T> T[] toArray(@NotNull T[] a) {
            return this.trades.toArray(a);
        }

        @Override
        public boolean add(VillagerTrades.ItemListing listing) {
            return this.trades.add(listing);
        }

        @Override
        public boolean remove(Object o) {
            return this.trades.remove(o);
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            return this.trades.containsAll(c);
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends VillagerTrades.ItemListing> c) {
            return this.trades.addAll(c);
        }

        @Override
        public boolean addAll(int index, @NotNull Collection<? extends VillagerTrades.ItemListing> c) {
            return this.trades.addAll(index, c);
        }

        @Override
        public boolean removeAll(@NotNull Collection<?> c) {
            return this.trades.removeAll(c);
        }

        @Override
        public boolean retainAll(@NotNull Collection<?> c) {
            return this.trades.retainAll(c);
        }

        @Override
        public void clear() {
            this.trades.clear();
        }

        @Override
        public VillagerTrades.ItemListing get(int index) {
            return this.trades.get(index);
        }

        @Override
        public VillagerTrades.ItemListing set(int index, VillagerTrades.ItemListing element) {
            return this.trades.set(index, element);
        }

        @Override
        public void add(int index, VillagerTrades.ItemListing element) {
            this.trades.add(index, element);
        }

        @Override
        public VillagerTrades.ItemListing remove(int index) {
            return this.trades.remove(index);
        }

        @Override
        public int indexOf(Object o) {
            return this.trades.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return this.trades.lastIndexOf(o);
        }

        @NotNull
        @Override
        public ListIterator<VillagerTrades.ItemListing> listIterator() {
            return this.trades.listIterator();
        }

        @NotNull
        @Override
        public ListIterator<VillagerTrades.ItemListing> listIterator(int index) {
            return this.trades.listIterator(index);
        }

        @NotNull
        @Override
        public List<VillagerTrades.ItemListing> subList(int fromIndex, int toIndex) {
            return this.trades.subList(fromIndex, toIndex);
        }

        public void add(ItemLike item, int emeralds, int itemCount, int maxUses, int xpGain, boolean sellToVillager) {
            this.add(new SimpleItemTrade(() -> item, emeralds, itemCount, maxUses, xpGain, 0.05F, sellToVillager));
        }

        public void add(ItemLike item, int emeralds, int itemCount, int maxUses, int xpGain, float priceMultiplier, boolean sellToVillager) {
            this.add(new SimpleItemTrade(() -> item, emeralds, itemCount, maxUses, xpGain, priceMultiplier, sellToVillager));
        }

        public void add(Supplier<? extends ItemLike> item, int emeralds, int itemCount, int maxUses, int xpGain, boolean sellToVillager) {
            this.add(new SimpleItemTrade(item, emeralds, itemCount, maxUses, xpGain, 0.05F, sellToVillager));
        }

        public void add(Supplier<? extends ItemLike> item, int emeralds, int itemCount, int maxUses, int xpGain, float priceMultiplier, boolean sellToVillager) {
            this.add(new SimpleItemTrade(item, emeralds, itemCount, maxUses, xpGain, priceMultiplier, sellToVillager));
        }
    }

    private record SimpleItemTrade(Supplier<? extends ItemLike> item, int emeralds, int count, int maxUses, int xpGain, float priceMultiplier, boolean sellToVillager) implements VillagerTrades.ItemListing {

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
            ItemStack emeralds = new ItemStack(Items.EMERALD, this.emeralds);
            ItemStack item = new ItemStack(this.item.get(), this.count);
            return new MerchantOffer(this.sellToVillager ? item : emeralds, this.sellToVillager ? emeralds : item, this.maxUses, this.xpGain, this.priceMultiplier);
        }
    }
}
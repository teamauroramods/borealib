package com.teamaurora.borealib.impl.event.entity.fabric;

import com.mojang.logging.LogUtils;
import com.teamaurora.borealib.api.event.entity.v1.TradeEvents;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ApiStatus.Internal
public class BorealibTradesLoader {

    private static final Map<VillagerProfession, Int2ObjectMap<VillagerTrades.ItemListing[]>> VANILLA_TRADES = new HashMap<>();
    private static final Int2ObjectMap<VillagerTrades.ItemListing[]> WANDERER_TRADES = new Int2ObjectOpenHashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();

    static {
        VillagerTrades.TRADES.forEach((key, value) -> {
            Int2ObjectMap<VillagerTrades.ItemListing[]> copy = new Int2ObjectOpenHashMap<>();
            value.int2ObjectEntrySet().forEach(ent -> copy.put(ent.getIntKey(), Arrays.copyOf(ent.getValue(), ent.getValue().length)));
            VANILLA_TRADES.put(key, copy);
        });
        VillagerTrades.WANDERING_TRADER_TRADES.int2ObjectEntrySet().forEach(e -> WANDERER_TRADES.put(e.getIntKey(), Arrays.copyOf(e.getValue(), e.getValue().length)));
    }

    public static void init() {
        initWanderer();
        initProfessions();
    }

    private static void initWanderer() {
        TradeEvents.TradeList generic = new TradeEvents.TradeList();
        TradeEvents.TradeList rare = new TradeEvents.TradeList();
        generic.addAll(Arrays.asList(WANDERER_TRADES.get(1)));
        rare.addAll(Arrays.asList(WANDERER_TRADES.get(2)));

        TradeEvents.WANDERER.invoker().modifyTrades(new TradeEvents.ModifyWanderer.Context() {
            @Override
            public TradeEvents.TradeList getGeneric() {
                return generic;
            }

            @Override
            public TradeEvents.TradeList getRare() {
                return rare;
            }
        });

        VillagerTrades.WANDERING_TRADER_TRADES.put(1, generic.toArray(new VillagerTrades.ItemListing[0]));
        VillagerTrades.WANDERING_TRADER_TRADES.put(2, rare.toArray(new VillagerTrades.ItemListing[0]));
    }

    private static void initProfessions() {
        for (VillagerProfession prof : RegistryWrapper.VILLAGER_PROFESSIONS) {
            Map<Integer, VillagerTrades.ItemListing[]> vanillaTrades = VANILLA_TRADES.get(prof);
            Map<Integer, TradeEvents.TradeList> newTrades = new Int2ObjectOpenHashMap<>();
            if (vanillaTrades != null) {
                // Create trades for each
                for (Map.Entry<Integer, VillagerTrades.ItemListing[]> entry : vanillaTrades.entrySet()) {
                    TradeEvents.TradeList registry = new TradeEvents.TradeList();
                    registry.addAll(Arrays.asList(entry.getValue()));
                    newTrades.put(entry.getKey(), registry);
                }
            } else {
                vanillaTrades = Collections.emptyMap();
                for (int i = 1; i <= 5; i++)
                    newTrades.put(i, new TradeEvents.TradeList());
            }
            int minTier = vanillaTrades.keySet().stream().mapToInt(Integer::intValue).min().orElse(1);
            int maxTier = vanillaTrades.keySet().stream().mapToInt(Integer::intValue).max().orElse(5);
            for (int i = minTier; i <= maxTier; i++) {
                if (!newTrades.containsKey(i)) {
                    LOGGER.warn(RegistryWrapper.VILLAGER_PROFESSIONS.getKey(prof) + " Villager Trades for tier: " + i + " didn't exist, adding");
                    newTrades.put(i, new TradeEvents.TradeList());
                }
            }
            TradeEvents.VILLAGER.invoker().modifyTrades(new TradeEvents.ModifyVillager.Context() {
                @Override
                public VillagerProfession getProfession() {
                    return prof;
                }

                @Override
                public TradeEvents.TradeList getTrades(int tier) {
                    Validate.inclusiveBetween(minTier, maxTier, tier, "Tier must be between " + minTier + " and " + maxTier);
                    TradeEvents.TradeList registry = newTrades.get(tier);
                    if (registry == null)
                        throw new IllegalStateException("No registered " + RegistryWrapper.VILLAGER_PROFESSIONS.getKey(prof) + " Villager Trades for tier: " + tier + ". Valid tiers: " + newTrades.keySet().stream().sorted().map(i -> Integer.toString(i)).collect(Collectors.joining(", ")));
                    return registry;
                }

                @Override
                public int getMinTier() {
                    return minTier;
                }

                @Override
                public int getMaxTier() {
                    return maxTier;
                }
            });
            Int2ObjectMap<VillagerTrades.ItemListing[]> modifiedTrades = new Int2ObjectOpenHashMap<>();
            newTrades.forEach((key, value) -> modifiedTrades.put(key.intValue(), value.toArray(new VillagerTrades.ItemListing[0])));
            VillagerTrades.TRADES.put(prof, modifiedTrades);
        }
    }
}
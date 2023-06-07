package com.teamaurora.borealib.api.datagen.v1.providers.loot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class MagnetosphereLootProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;
    private final List<LootTableProvider.SubProviderEntry> subProviders;
    private static final List<SubProviderEntry> VANILLA_PROVIDERS = ImmutableList.of(
            new SubProviderEntry(VanillaFishingLoot::new, LootContextParamSets.FISHING),
            new SubProviderEntry(VanillaChestLoot::new, LootContextParamSets.CHEST),
            new SubProviderEntry(VanillaEntityLoot::new, LootContextParamSets.ENTITY),
            new SubProviderEntry(VanillaBlockLoot::new, LootContextParamSets.BLOCK),
            new SubProviderEntry(VanillaPiglinBarterLoot::new, LootContextParamSets.PIGLIN_BARTER),
            new SubProviderEntry(VanillaGiftLoot::new, LootContextParamSets.GIFT));

    public MagnetosphereLootProvider(PackOutput packOutput) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
        this.subProviders = new ArrayList<>();
    }

    public MagnetosphereLootProvider add(Supplier<LootTableSubProvider> subProvider, LootContextParamSet paramSet) {
        this.subProviders.add(new LootTableProvider.SubProviderEntry(subProvider, paramSet));
        return this;
    }


    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Map<ResourceLocation, LootTable> lootTables = new HashMap<>();
        this.subProviders.forEach((subProviderEntry) -> {
            subProviderEntry.provider().get().generate((resourceLocation, builder) -> {
                if (lootTables.put(resourceLocation, builder.setParamSet(subProviderEntry.paramSet()).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + resourceLocation);
                }
            });
        });

        Map<ResourceLocation, LootTable> registry = new HashMap<>(lootTables);
        try {
            VANILLA_PROVIDERS.forEach(provider -> provider.provider.get().generate((name, builder) -> {
                if (registry.put(name, builder.build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + name);
                }
            }));
        } catch (Throwable ignored) {
        }

        ValidationContext validationContext = new ValidationContext(LootContextParamSets.ALL_PARAMS, resourcelocationx -> null, registry::get);
        lootTables.forEach((resourceLocationx, lootTable) -> LootTables.validate(validationContext, resourceLocationx, lootTable));

        Multimap<String, String> problems = validationContext.getProblems();
        if (!problems.isEmpty()) {
            problems.forEach((string, string2) -> LOGGER.warn("Found validation problem in {}: {}", string, string2));
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            return CompletableFuture.allOf(lootTables.entrySet().stream().map((entry) -> {
                ResourceLocation resourceLocation = entry.getKey();
                LootTable lootTable = entry.getValue();
                Path path = this.pathProvider.json(resourceLocation);
                return DataProvider.saveStable(cachedOutput, LootTables.serialize(lootTable), path);
            }).toArray(CompletableFuture[]::new));
        }
    }

    @Override
    public String getName() {
        return "Loot Tables";
    }

    public record SubProviderEntry(Supplier<LootTableSubProvider> provider, LootContextParamSet paramSet) {
    }
}

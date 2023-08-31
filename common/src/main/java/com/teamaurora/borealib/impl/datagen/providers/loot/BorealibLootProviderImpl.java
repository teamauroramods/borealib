package com.teamaurora.borealib.impl.datagen.providers.loot;

import com.google.gson.JsonObject;
import com.teamaurora.borealib.api.datagen.v1.providers.loot.BorealibLootProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
public class BorealibLootProviderImpl {

    public static CompletableFuture<?> run(CachedOutput cachedOutput, BorealibLootProvider provider, LootContextParamSet paramSet, PackOutput.PathProvider pathProvider) {
        Map<ResourceLocation, LootTable> builders = new HashMap<>();
        provider.generate((id, builder) -> {
            if (builders.put(id, builder.setParamSet(paramSet).build()) != null)
                throw new IllegalStateException("Duplicate loot table " + id);
        });
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Map.Entry<ResourceLocation, LootTable> entry : builders.entrySet()) {
            JsonObject tableJson = (JsonObject) LootDataType.TABLE.parser().toJsonTree(entry.getValue());
            provider.injectConditions(entry.getKey(), tableJson);
            futures.add(DataProvider.saveStable(cachedOutput, tableJson, pathProvider.json(entry.getKey())));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }
}

package com.teamaurora.borealib.api.datagen.v1.providers.loot;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import com.teamaurora.borealib.impl.datagen.providers.loot.BorealibLootProviderImpl;
import com.teamaurora.borealib.impl.datagen.providers.loot.ConditionalBlockLootGenerator;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * @author ebo2022
 * @since 1.0
 */
public abstract class BorealibBlockLootProvider extends BlockLootSubProvider implements BorealibLootProvider {

    private final PackOutput.PathProvider pathProvider;
    private final Map<ResourceLocation, List<ResourceConditionProvider>> providers = new HashMap<>();

    protected BorealibBlockLootProvider(BorealibPackOutput output) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
    }

    /**
     * Registers all loot tables to be generated.
     * <p>Use the included {@link #add(Block, LootTable.Builder)} methods to register tables.
     */
    @Override
    public abstract void generate();

    /**
     * @deprecated Use {@link #withConditions(ResourceConditionProvider...)} instead
     */
    @Override
    @Deprecated
    public BiConsumer<ResourceLocation, LootTable.Builder> withConditions(BiConsumer<ResourceLocation, LootTable.Builder> exporter, ResourceConditionProvider... conditions) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a child block loot generator whose entries all receive the specified resource conditions.
     *
     * @param conditions The conditions to add
     * @return A child provider with the conditions to apply to its entries
     */
    protected BlockLootSubProvider withConditions(ResourceConditionProvider... conditions) {
        Preconditions.checkArgument(conditions.length > 0, "At least one resource condition is required");
        return new ConditionalBlockLootGenerator(this, conditions);
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        this.generate();
        for (Map.Entry<ResourceLocation, LootTable.Builder> entry : this.map.entrySet()) {
            ResourceLocation id = entry.getKey();
            if (id.equals(BuiltInLootTables.EMPTY))
                continue;
            biConsumer.accept(id, entry.getValue());
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return BorealibLootProviderImpl.run(cachedOutput, this, LootContextParamSets.BLOCK, this.pathProvider);
    }

    @Override
    public void addConditions(ResourceLocation id, ResourceConditionProvider... providers) {
        if (providers.length == 0)
            return;
        this.providers.computeIfAbsent(id, __ -> new ArrayList<>()).addAll(Arrays.asList(providers));
    }

    @Override
    public void injectConditions(ResourceLocation id, JsonObject json) {
        if (this.providers.containsKey(id))
            ResourceConditionProvider.write(json, this.providers.get(id).toArray(new ResourceConditionProvider[0]));
    }

    @Override
    public String getName() {
        return "Block Loot";
    }
}

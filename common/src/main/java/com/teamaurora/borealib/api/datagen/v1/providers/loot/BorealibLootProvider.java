package com.teamaurora.borealib.api.datagen.v1.providers.loot;

import com.google.common.base.Preconditions;
import com.teamaurora.borealib.api.datagen.v1.ConditionalDataProvider;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;

/**
 * A shared interface inherited by all Borealib-based loot generators which allows them to function as independent {@link DataProvider}s.
 *
 * @author ebo2022
 * @since 1.0
 */
@ApiStatus.NonExtendable
public interface BorealibLootProvider extends LootTableSubProvider, ConditionalDataProvider {

    /**
     * Creates an exporter that adds the specified conditions to every loot table registered by it.
     *
     * @param exporter   The original exporter
     * @param conditions The conditions to add
     * @return An exporter adding the conditions to each loot table registered by it
     */
    default BiConsumer<ResourceLocation, LootTable.Builder> withConditions(BiConsumer<ResourceLocation, LootTable.Builder> exporter, ResourceConditionProvider... conditions) {
        Preconditions.checkArgument(conditions.length > 0, "At least one resource condition is required");
        return (id, table) -> {
            this.addConditions(id, conditions);
            exporter.accept(id, table);
        };
    }
}

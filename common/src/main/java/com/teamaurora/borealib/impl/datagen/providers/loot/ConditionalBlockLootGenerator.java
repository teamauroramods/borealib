package com.teamaurora.borealib.impl.datagen.providers.loot;

import com.teamaurora.borealib.api.datagen.v1.providers.loot.BorealibBlockLootProvider;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.function.BiConsumer;

@ApiStatus.Internal
public class ConditionalBlockLootGenerator extends BlockLootSubProvider {
	private final BorealibBlockLootProvider parent;
	private final ResourceConditionProvider[] conditions;

	public ConditionalBlockLootGenerator(BorealibBlockLootProvider parent, ResourceConditionProvider[] conditions) {
		super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
		this.parent = parent;
		this.conditions = conditions;
	}

	@Override
	public void generate() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(Block block, LootTable.Builder lootTable) {
		this.parent.addConditions(block.getLootTable(), this.conditions);
		this.parent.add(block, lootTable);
	}
}
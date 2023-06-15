package com.teamaurora.borealib.impl.biome.modifier.info.forge;

import com.teamaurora.borealib.api.biome.v1.modifier.info.SpawnSettings;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

@ApiStatus.Internal
public class SpawnSettingsImpl implements SpawnSettings.Mutable {

    private final MobSpawnSettingsBuilder builder;

    SpawnSettingsImpl(MobSpawnSettingsBuilder builder) {
        this.builder = builder;
    }

    @Override
    public float getCreatureProbability() {
        return this.builder.getProbability();
    }

    @Override
    public Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners() {
        return this.builder.spawners;
    }

    @Override
    public Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts() {
        return this.builder.mobSpawnCosts;
    }

    @Override
    public Mutable setCreatureProbability(float probability) {
        this.builder.creatureGenerationProbability(probability);
        return this;
    }

    @Override
    public Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data) {
        this.builder.addSpawn(category, data);
        return this;
    }

    @Override
    public boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate) {
        boolean removed = false;
        for (MobCategory type : this.builder.getSpawnerTypes())
            if (this.builder.getSpawner(type).removeIf(data -> predicate.test(type, data)))
                removed = true;
        return removed;
    }

    @Override
    public Mutable setSpawnCost(EntityType<?> entityType, MobSpawnSettings.MobSpawnCost cost) {
        this.builder.mobSpawnCosts.put(entityType, cost);
        return this;
    }

    @Override
    public Mutable setSpawnCost(EntityType<?> entityType, double charge, double energyBudget) {
        this.builder.addMobCharge(entityType, charge, energyBudget);
        return this;
    }

    @Override
    public Mutable clearSpawnCost(EntityType<?> entityType) {
        this.getMobSpawnCosts().remove(entityType);
        return this;
    }
}

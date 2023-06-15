package com.teamaurora.borealib.impl.biome.modifier.info.fabric;

import com.google.common.collect.Maps;
import com.teamaurora.borealib.api.biome.v1.modifier.info.SpawnSettings;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

@ApiStatus.Internal
public class SpawnSettingsImpl implements SpawnSettings {

    private final MobSpawnSettings spawnSettings;

    SpawnSettingsImpl(Biome biome) {
        this.spawnSettings = biome.getMobSettings();
    }

    @Override
    public float getCreatureProbability() {
        return this.spawnSettings.getCreatureProbability();
    }

    @Override
    public Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners() {
        return Maps.transformValues(this.spawnSettings.spawners, WeightedRandomList::unwrap);
    }

    @Override
    public Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts() {
        return this.spawnSettings.mobSpawnCosts;
    }

    public static class Mutable extends SpawnSettingsImpl implements SpawnSettings.Mutable {

        private final BiomeModificationContext.SpawnSettingsContext context;

        Mutable(Biome biome, BiomeModificationContext.SpawnSettingsContext context) {
            super(biome);
            this.context = context;
        }

        @Override
        public SpawnSettings.Mutable setCreatureProbability(float probability) {
            this.context.setCreatureSpawnProbability(probability);
            return this;
        }

        @Override
        public SpawnSettings.Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data) {
            this.context.addSpawn(category, data);
            return this;
        }

        @Override
        public boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate) {
            return this.context.removeSpawns(predicate);
        }

        @Override
        public SpawnSettings.Mutable setSpawnCost(EntityType<?> entityType, MobSpawnSettings.MobSpawnCost cost) {
            this.context.setSpawnCost(entityType, cost.charge(), cost.energyBudget());
            return this;
        }

        @Override
        public SpawnSettings.Mutable setSpawnCost(EntityType<?> entityType, double charge, double energyBudget) {
            this.context.setSpawnCost(entityType, charge, energyBudget);
            return this;
        }

        @Override
        public SpawnSettings.Mutable clearSpawnCost(EntityType<?> entityType) {
            this.context.clearSpawnCost(entityType);
            return this;
        }
    }
}

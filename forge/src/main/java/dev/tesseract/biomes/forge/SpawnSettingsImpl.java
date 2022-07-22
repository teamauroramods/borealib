package dev.tesseract.biomes.forge;

import dev.tesseract.biomes.SpawnSettings;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public class SpawnSettingsImpl implements SpawnSettings {

    protected final MobSpawnSettingsBuilder parent;

    public SpawnSettingsImpl(MobSpawnSettingsBuilder parent) {
        this.parent = parent;
    }

    @Override
    public float getCreatureProbability() {
        return this.parent.getProbability();
    }

    @Override
    public Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners() {
        return this.parent.spawners;
    }

    @Override
    public Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts() {
        return this.parent.mobSpawnCosts;
    }

    public static class Mutable extends SpawnSettingsImpl implements SpawnSettings.Mutable {

        public Mutable(MobSpawnSettingsBuilder parent) {
            super(parent);
        }

        @Override
        public SpawnSettings.Mutable setCreatureProbability(float probability) {
            this.parent.creatureGenerationProbability(probability);
            return this;
        }

        @Override
        public SpawnSettings.Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data) {
            this.parent.addSpawn(category, data);
            return this;
        }

        @Override
        public boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate) {
            boolean removed = false;
            for (MobCategory type : this.parent.getSpawnerTypes()) {
                if (this.parent.getSpawner(type).removeIf(data -> predicate.test(type, data))) {
                    removed = true;
                }
            }
            return removed;
        }

        @Override
        public SpawnSettings.Mutable setSpawnCost(EntityType<?> entityType, MobSpawnSettings.MobSpawnCost cost) {
            this.parent.addMobCharge(entityType, cost.getCharge(), cost.getEnergyBudget());
            return this;
        }

        @Override
        public SpawnSettings.Mutable setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit) {
            this.parent.addMobCharge(entityType, mass, gravityLimit);
            return this;
        }

        @Override
        public SpawnSettings.Mutable clearSpawnCost(EntityType<?> entityType) {
            getMobSpawnCosts().remove(entityType);
            return this;
        }
    }
}

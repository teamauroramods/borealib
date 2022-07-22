package dev.tesseract.biomes.impl;

import dev.tesseract.biomes.SpawnSettings;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;
import java.util.Map;

public class ImmutableSpawnSettings implements SpawnSettings {

    protected final MobSpawnSettings settings;

    public ImmutableSpawnSettings(Biome biome) {
        this(biome.getMobSettings());
    }

    public ImmutableSpawnSettings(MobSpawnSettings settings) {
        this.settings = settings;
    }

    @Override
    public float getCreatureProbability() {
        return this.settings.getCreatureProbability();
    }

    @Override
    public Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners() {
        return null;
    }

    @Override
    public Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts() {
        return null;
    }
}

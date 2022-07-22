package dev.tesseract.biomes.fabric;

import dev.tesseract.biomes.SpawnSettings;
import dev.tesseract.biomes.impl.ImmutableSpawnSettings;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiPredicate;

@ApiStatus.Internal
public class MutableSpawnSettingsImpl extends ImmutableSpawnSettings implements SpawnSettings.Mutable {

    private final BiomeModificationContext.SpawnSettingsContext parent;

    public MutableSpawnSettingsImpl(Biome biome, BiomeModificationContext.SpawnSettingsContext context) {
        super(biome);
        this.parent = context;
    }

    @Override
    public Mutable setCreatureProbability(float probability) {
        this.parent.setCreatureSpawnProbability(probability);
        return this;
    }

    @Override
    public Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data) {
        this.parent.addSpawn(category, data);
        return this;
    }

    @Override
    public boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate) {
        return this.parent.removeSpawns(predicate);
    }

    @Override
    public Mutable setSpawnCost(EntityType<?> entityType, MobSpawnSettings.MobSpawnCost cost) {
        this.parent.setSpawnCost(entityType, cost.getCharge(), cost.getEnergyBudget());
        return this;
    }

    @Override
    public Mutable setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit) {
        this.parent.setSpawnCost(entityType, mass, gravityLimit);
        return this;
    }

    @Override
    public Mutable clearSpawnCost(EntityType<?> entityType) {
        this.parent.clearSpawnCost(entityType);
        return this;
    }
}

/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.teamaurora.borealib.api.biome.v1.modifier.info;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * Controls what mobs spawn in a given biome.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface SpawnSettings {

    /**
     * @return The current creature spawn probability
     */
    float getCreatureProbability();

    /**
     * @return An unmodifiable map of the current spawners
     */
    Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners();

    /**
     * @return An unmodifiable map of the current spawn costs
     */
    Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts();

    /**
     * Extends the mob spawn settings to allow them to be modified.
     *
     * @since 1.0
     */
    interface Mutable extends SpawnSettings {

        /**
         * Sets a new creature spawn probability.
         *
         * @param probability The new creature spawn probability
         */
        Mutable setCreatureProbability(float probability);

        /**
         * Adds a new mob spawn.
         *
         * @param category The category of the mob spawn
         * @param data     The spawner data
         */
        Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data);

        /**
         * Removes any mob spawns that match the specified predicate.
         *
         * @param predicate The predicate to determine which spawns to remove
         * @return Whether any spawns were removed
         */
        boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate);

        /**
         * Sets a new spawn cost for the specified entity.
         *
         * @param entityType The entity type to set the spawn cost for
         * @param cost       The new spawn cost
         */
        Mutable setSpawnCost(EntityType<?> entityType, MobSpawnSettings.MobSpawnCost cost);

        /**
         * Sets a new spawn cost for the specified entity.
         *
         * @param entityType   The entity type to set the spawn cost for
         * @param charge       The charge of the spawn cost
         * @param energyBudget The energy budget for the spawn cost
         */
        Mutable setSpawnCost(EntityType<?> entityType, double charge, double energyBudget);

        /**
         * Clears the spawn costs for the given entity.
         *
         * @param entityType The entity type to clear the spawn cost for
         */
        Mutable clearSpawnCost(EntityType<?> entityType);
    }
}
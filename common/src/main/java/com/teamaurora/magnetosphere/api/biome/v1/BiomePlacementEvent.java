package com.teamaurora.magnetosphere.api.biome.v1;

import com.mojang.datafixers.util.Pair;
import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.function.Consumer;

@FunctionalInterface
public interface BiomePlacementEvent {

    Event<BiomePlacementEvent> EVENT = Event.createLoop(BiomePlacementEvent.class);

    void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer);
}

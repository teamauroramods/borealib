package com.teamaurora.magnetosphere.core.registry;

import com.mojang.serialization.Lifecycle;
import com.teamaurora.magnetosphere.api.entity.v1.CustomBoatType;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class MagnetosphereRegistries {

    public static final MappedRegistry<CustomBoatType> BOAT_TYPES = new MappedRegistry<>(ResourceKey.createRegistryKey(Magnetosphere.location("boat_type")), Lifecycle.stable());

    private MagnetosphereRegistries() {
    }
}

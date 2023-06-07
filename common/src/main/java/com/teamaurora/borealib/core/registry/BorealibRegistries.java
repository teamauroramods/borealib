package com.teamaurora.borealib.core.registry;

import com.mojang.serialization.Lifecycle;
import com.teamaurora.borealib.api.entity.v1.CustomBoatType;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class BorealibRegistries {

    public static final MappedRegistry<CustomBoatType> BOAT_TYPES = new MappedRegistry<>(ResourceKey.createRegistryKey(Borealib.location("boat_type")), Lifecycle.stable());

    private BorealibRegistries() {}
}

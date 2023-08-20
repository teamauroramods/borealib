package com.teamaurora.borealib.core.registry;

import com.teamaurora.borealib.api.entity.v1.CustomBoat;
import com.teamaurora.borealib.api.entity.v1.CustomChestBoat;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.jetbrains.annotations.ApiStatus;

public final class BorealibEntityTypes {

    @ApiStatus.Internal
    public static final RegistryWrapper.EntityProvider ENTITIES = RegistryWrapper.entityProvider(Borealib.MOD_ID);
    public static final RegistryReference<EntityType<CustomBoat>> BOAT = ENTITIES.register("boat", () -> EntityType.Builder.<CustomBoat>of(CustomBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("borealib:boat"));
    public static final RegistryReference<EntityType<CustomChestBoat>> CHEST_BOAT = ENTITIES.register("chest_boat", () -> EntityType.Builder.<CustomChestBoat>of(CustomChestBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("borealib:chest_boat"));
}
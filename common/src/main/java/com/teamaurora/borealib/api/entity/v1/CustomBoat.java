package com.teamaurora.borealib.api.entity.v1;

import com.teamaurora.borealib.api.base.v1.util.NBTConstants;
import com.teamaurora.borealib.api.item.v1.CustomBoatItem;
import com.teamaurora.borealib.core.registry.BorealibEntityTypes;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CustomBoat extends Boat {

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(CustomBoat.class, EntityDataSerializers.INT);

    public CustomBoat(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    public CustomBoat(Level level, double d, double e, double f) {
        this(BorealibEntityTypes.BOAT.get(), level);
        this.setPos(d, e, f);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = d;
        this.yo = e;
        this.zo = f;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getBoatCustomType() == null) {
            Boat boat = new Boat(this.level, this.getX(), this.getY(), this.getZ());
            boat.copyPosition(this);
            if (this.hasCustomName()) {
                boat.setCustomName(this.getCustomName());
                boat.setCustomNameVisible(this.isCustomNameVisible());
            }

            boat.setInvulnerable(this.isInvulnerable());

            this.level.addFreshEntity(boat);
            if (this.isPassenger())
                boat.startRiding(this.getVehicle(), true);

            for (Entity passenger : this.getPassengers())
                passenger.startRiding(boat, true);

            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public void setVariant(Type type) {
    }

    @Override
    public Type getVariant() {
        return Type.OAK;
    }

    @Override
    public Item getDropItem() {
        CustomBoatType type = this.getBoatCustomType();
        return type != null ? CustomBoatItem.getBoatItem(type, false) : null;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, -1);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        ResourceLocation id = BorealibRegistries.BOAT_TYPES.getKey(this.getBoatCustomType());
        if (id != null)
            compound.putString("Type", id.toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("Type", NBTConstants.STRING))
            this.setPollenType(BorealibRegistries.BOAT_TYPES.get(new ResourceLocation(compound.getString("Type"))));
    }

    public void setPollenType(@Nullable CustomBoatType boatType) {
        this.entityData.set(DATA_ID_TYPE, boatType == null ? -1 : BorealibRegistries.BOAT_TYPES.getId(boatType));
    }

    public CustomBoatType getBoatCustomType() {
        int id = this.entityData.get(DATA_ID_TYPE);
        return id == -1 ? null : BorealibRegistries.BOAT_TYPES.byId(id);
    }
}
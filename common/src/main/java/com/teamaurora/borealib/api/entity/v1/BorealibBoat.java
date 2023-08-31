package com.teamaurora.borealib.api.entity.v1;

import com.teamaurora.borealib.api.base.v1.util.NBTConstants;
import com.teamaurora.borealib.api.item.v1.BorealibBoatItem;
import com.teamaurora.borealib.core.registry.BorealibEntityTypes;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BorealibBoat extends Boat {

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(BorealibBoat.class, EntityDataSerializers.INT);

    public BorealibBoat(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    public BorealibBoat(Level level, double d, double e, double f) {
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
            Boat boat = new Boat(this.level(), this.getX(), this.getY(), this.getZ());
            boat.copyPosition(this);
            if (this.hasCustomName()) {
                boat.setCustomName(this.getCustomName());
                boat.setCustomNameVisible(this.isCustomNameVisible());
            }

            boat.setInvulnerable(this.isInvulnerable());

            this.level().addFreshEntity(boat);
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
        BorealibBoatType type = this.getBoatCustomType();
        return type != null ? BorealibBoatItem.getBoatItem(type, false) : null;
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
            this.setCustomType(BorealibRegistries.BOAT_TYPES.get(new ResourceLocation(compound.getString("Type"))));
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
        this.lastYd = this.getDeltaMovement().y;
        if (!this.isPassenger()) {
            if (bl) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != Boat.Status.ON_LAND) {
                        this.resetFallDistance();
                        return;
                    }

                    this.causeFallDamage(this.fallDistance, 1.0F, this.damageSources().fall());
                    if (!this.level().isClientSide && !this.isRemoved()) {
                        this.kill();
                        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            int i;
                            for(i = 0; i < 3; ++i) {
                                this.spawnAtLocation(this.getBoatCustomType().planks().get());
                            }

                            for(i = 0; i < 2; ++i) {
                                this.spawnAtLocation(Items.STICK);
                            }
                        }
                    }
                }

                this.resetFallDistance();
            } else if (!this.level().getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && d < 0.0) {
                this.fallDistance -= (float)d;
            }

        }
    }

    public void setCustomType(@Nullable BorealibBoatType boatType) {
        this.entityData.set(DATA_ID_TYPE, boatType == null ? -1 : BorealibRegistries.BOAT_TYPES.getId(boatType));
    }

    public BorealibBoatType getBoatCustomType() {
        int id = this.entityData.get(DATA_ID_TYPE);
        return id == -1 ? null : BorealibRegistries.BOAT_TYPES.byId(id);
    }

}
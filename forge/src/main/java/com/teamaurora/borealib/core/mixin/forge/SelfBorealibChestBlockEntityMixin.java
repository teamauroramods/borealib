package com.teamaurora.borealib.core.mixin.forge;

import com.teamaurora.borealib.api.block.v1.entity.compat.BorealibChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BorealibChestBlockEntity.class)
public abstract class SelfBorealibChestBlockEntityMixin extends BlockEntity {

    public SelfBorealibChestBlockEntityMixin(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
    }

    @Override
    public AABB getRenderBoundingBox() {
        BlockPos worldPos = this.worldPosition;
        int x = worldPos.getX();
        int y = worldPos.getY();
        int z = worldPos.getZ();
        return new AABB(x - 1, y, z - 1, x + 2, y + 2, z + 2);
    }
}

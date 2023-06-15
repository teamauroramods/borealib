package com.teamaurora.borealib.core.mixin;

import com.teamaurora.borealib.core.extensions.BlockEntityTypeExtension;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashSet;
import java.util.Set;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin implements BlockEntityTypeExtension {

    @Shadow
    @Final
    @Mutable
    private Set<Block> validBlocks;

    @Override
    public void borealib$addAdditionalBlockTypes(Block... blocks) {
        Set<Block> b = new HashSet<>(validBlocks);
        for (Block block : blocks)
            b.add(block);
        validBlocks = b;
    }
}
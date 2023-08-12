package com.teamaurora.borealib.api.block.v1;

import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class BorealibPressurePlateBlock extends PressurePlateBlock {
    public BorealibPressurePlateBlock(Sensitivity sensitivity, Properties properties, BlockSetType blockSetType) {
        super(sensitivity, properties, blockSetType);
    }
}

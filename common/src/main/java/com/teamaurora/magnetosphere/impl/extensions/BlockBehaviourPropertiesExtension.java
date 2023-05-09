package com.teamaurora.magnetosphere.impl.extensions;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Function;

public interface BlockBehaviourPropertiesExtension {

    BlockBehaviour.Properties colorFunction(Function<BlockState, MaterialColor> colorFunction);
}

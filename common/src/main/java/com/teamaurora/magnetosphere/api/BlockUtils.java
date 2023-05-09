package com.teamaurora.magnetosphere.api;

import com.teamaurora.magnetosphere.impl.extensions.BlockBehaviourPropertiesExtension;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Function;

public interface BlockUtils {

    static BlockBehaviour.Properties colorFunction(BlockBehaviour.Properties properties, Function<BlockState, MaterialColor> function) {
        return ((BlockBehaviourPropertiesExtension) properties).colorFunction(function);
    }
}

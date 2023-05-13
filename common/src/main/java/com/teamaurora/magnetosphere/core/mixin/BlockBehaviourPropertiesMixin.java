package com.teamaurora.magnetosphere.core.mixin;

import com.teamaurora.magnetosphere.core.extensions.BlockBehaviourPropertiesExtension;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;

@Mixin(BlockBehaviour.Properties.class)
public class BlockBehaviourPropertiesMixin implements BlockBehaviourPropertiesExtension {

    @Shadow
    Function<BlockState, MaterialColor> materialColor;

    @Override
    public BlockBehaviour.Properties colorFunction(Function<BlockState, MaterialColor> colorFunction) {
        this.materialColor = colorFunction;
        return ((BlockBehaviour.Properties) (Object) this);
    }
}

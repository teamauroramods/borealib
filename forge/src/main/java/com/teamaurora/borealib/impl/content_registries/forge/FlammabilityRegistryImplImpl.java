package com.teamaurora.borealib.impl.content_registries.forge;

import com.teamaurora.borealib.core.mixin.forge.FireBlockAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FlammabilityRegistryImplImpl {

    public static synchronized void register(Block fireBlock, int encouragement, int flammability, Block... blocks) {
        if (!(fireBlock instanceof FireBlock))
            throw new IllegalStateException("Block " + fireBlock + " is not an instance of FireBlock");
        for (Block block : blocks) {
            ((FireBlockAccessor) fireBlock).invokeSetFlammable(block, encouragement, flammability);
        }
    }
}

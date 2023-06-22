package com.teamaurora.borealib.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamaurora.borealib.api.block.v1.FancyRenderBlock;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(ItemBlockRenderTypes.class)
public class ItemBlockRenderTypesMixin {

    @WrapOperation(method = "getChunkRenderType", constant = @Constant(classValue = LeavesBlock.class))
    private static boolean extendChunkRenderType(Object obj, Operation<Boolean> original) {
        return original.call(obj) || obj instanceof FancyRenderBlock;
    }

    @WrapOperation(method = "getMovingBlockRenderType", constant = @Constant(classValue = LeavesBlock.class))
    private static boolean extendMovingBlockRenderTypes(Object obj, Operation<Boolean> original) {
        return original.call(obj) || obj instanceof FancyRenderBlock;
    }
}

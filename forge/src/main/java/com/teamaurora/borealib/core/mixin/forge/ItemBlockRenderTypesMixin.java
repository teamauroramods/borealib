package com.teamaurora.borealib.core.mixin.forge;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamaurora.borealib.api.block.v1.FancyRenderBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(ItemBlockRenderTypes.class)
public class ItemBlockRenderTypesMixin {

    @WrapOperation(method = "getRenderLayers", constant = @Constant(classValue = LeavesBlock.class), remap = false)
    private static boolean addModLeavesBlocks(Object obj, Operation<Boolean> original) {
        return original.call(obj) || obj instanceof FancyRenderBlock;
    }
}

package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.api.item.v1.BEWLRBlockItem;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void handleBewlr(Item.Properties properties, CallbackInfo ci) {
        if ((Item) (Object) this instanceof BEWLRBlockItem bewlrBlockItem) {
            try {
                BuiltinItemRendererRegistry.INSTANCE.register(bewlrBlockItem, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
                    BEWLRBlockItem.LazyBEWLR lazyBEWLR = bewlrBlockItem.getBewlr().get();
                    BlockEntityWithoutLevelRenderer value = lazyBEWLR.value;
                    if (value != null)
                        value.renderByItem(stack, mode, matrices, vertexConsumers, light, overlay);
                    else
                        lazyBEWLR.cache(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()).renderByItem(stack, mode, matrices, vertexConsumers, light, overlay);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

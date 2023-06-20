package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.api.item.v1.BEWLRBlockItem;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(BEWLRBlockItem.class)
public abstract class SelfBEWLRBlockItemMixin extends BlockItem {

    @Shadow
    @Final
    @Nullable
    private Supplier<BEWLRBlockItem.LazyBEWLR> bewlr;


    public SelfBEWLRBlockItemMixin(Block block, Properties properties) {
        super(block, properties);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addForgeItemProperties(Item.Properties properties, CallbackInfo info) {
        try {
            BuiltinItemRendererRegistry.INSTANCE.register(this, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
                BEWLRBlockItem.LazyBEWLR lazyBEWLR = this.bewlr.get();
                BlockEntityWithoutLevelRenderer value = lazyBEWLR.value;
                if (value != null) {
                    value.renderByItem(stack, mode, matrices, vertexConsumers, light, overlay);
                } else {
                    lazyBEWLR.cache(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()).renderByItem(stack, mode, matrices, vertexConsumers, light, overlay);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }}

package com.teamaurora.borealib.api.item.v1;

import com.teamaurora.borealib.api.base.v1.platform.EnvExecutor;
import com.teamaurora.borealib.api.base.v1.platform.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class BEWLRBlockItem extends BlockItem {
    @Nullable
    private final Supplier<LazyBEWLR> bewlr;

    public BEWLRBlockItem(Block block, Properties properties, Supplier<Callable<LazyBEWLR>> bewlr) {
        super(block, properties);
        LazyBEWLR lazyBEWLR = EnvExecutor.unsafeCallWhenOn(Environment.CLIENT, bewlr);
        this.bewlr = lazyBEWLR == null ? null : () -> lazyBEWLR;
    }

    public Supplier<LazyBEWLR> getBewlr() {
        return this.bewlr;
    }

    public static final class LazyBEWLR {
        private final BiFunction<BlockEntityRenderDispatcher, EntityModelSet, BlockEntityWithoutLevelRenderer> cacheFunction;
        public BlockEntityWithoutLevelRenderer value;

        public LazyBEWLR(BiFunction<BlockEntityRenderDispatcher, EntityModelSet, BlockEntityWithoutLevelRenderer> cacheFunction) {
            this.cacheFunction = cacheFunction;
        }

        public BlockEntityWithoutLevelRenderer cache(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
            return this.value = this.cacheFunction.apply(dispatcher, modelSet);
        }
    }
}
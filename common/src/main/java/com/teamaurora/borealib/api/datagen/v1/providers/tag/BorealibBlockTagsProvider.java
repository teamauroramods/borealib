package com.teamaurora.borealib.api.datagen.v1.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public abstract class BorealibBlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {

    public BorealibBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, Registries.BLOCK, lookup, block -> block.builtInRegistryHolder().key());
    }
}

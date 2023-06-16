package com.teamaurora.borealib.api.datagen.v1.providers.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public abstract class BorealibItemTagsProvider extends ItemTagsProvider {
    public BorealibItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, BorealibBlockTagsProvider blockTagsProvider) {
        super(packOutput, completableFuture,  blockTagsProvider.contentsGetter());
    }
}

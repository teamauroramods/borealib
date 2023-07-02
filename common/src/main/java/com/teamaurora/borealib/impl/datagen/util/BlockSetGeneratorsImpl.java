package com.teamaurora.borealib.impl.datagen.util;

import com.google.common.base.Preconditions;
import com.teamaurora.borealib.api.block.v1.set.BlockSet;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibLanguageProvider;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BlockSetGeneratorsImpl {

    public static void createEnUsLanguage(BorealibLanguageProvider.TranslationRegistry registry, BlockSet... blockSets) {
        Preconditions.checkArgument(blockSets.length > 0, "Must generate data for at least 1 wood set");

        for (BlockSet<?> blockSet : blockSets) {
            blockSet.getBlocksByVariant().forEach((variant, block) -> {
                if (variant.shouldGenerateAutoLanguage())
                    registry.add(block.get(), BorealibLanguageProvider.format(block));
            });
            blockSet.getItemsByVariant().forEach((variant, item) -> {
                if (variant.shouldGenerateAutoLanguage())
                    registry.add(item.get(), BorealibLanguageProvider.format(item));
            });
        }
    }
}

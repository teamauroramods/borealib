package com.teamaurora.borealib.api.datagen.v1.util;

import com.teamaurora.borealib.api.block.v1.set.BlockSet;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibLanguageProvider;
import com.teamaurora.borealib.impl.datagen.util.BlockSetGeneratorsImpl;

/**
 * Contains methods for generating generic data for {@link BlockSet}s.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface BlockSetGenerators {

    /**
     * Generates formatted english translation keys for block and item variants that need it.
     *
     * @param registry  The translation registry to add to
     * @param blockSets The block sets to translate
     */
    static void createEnUsLanguage(BorealibLanguageProvider.TranslationRegistry registry, BlockSet<?>... blockSets) {
        BlockSetGeneratorsImpl.createEnUsLanguage(registry, blockSets);
    }
}

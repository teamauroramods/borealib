package com.teamaurora.borealib.api.biome.v1.modifier;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.core.registry.BorealibRegistries;

import java.util.function.Consumer;
import java.util.function.Function;

public interface BiomeModifierAction extends Consumer<BiomeInfo.Mutable> {

    Codec<BiomeModifierAction> CODEC = BorealibRegistries.BIOME_MODIFIER_ACTION_TYPES.byNameCodec().dispatch(BiomeModifierAction::type, Function.identity());

    Codec<? extends BiomeModifierAction> type();

    Stage applicationStage();

    enum Stage {
        ADDITIONS,
        REMOVALS,
        REPLACEMENTS,
        POST_PROCESSING
    }
}

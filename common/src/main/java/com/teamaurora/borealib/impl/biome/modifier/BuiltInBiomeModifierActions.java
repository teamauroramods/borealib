package com.teamaurora.borealib.impl.biome.modifier;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifierAction;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BuiltInBiomeModifierActions {

    private static final Codec<SkyColorModification> SKY_COLOR_MODIFICATION_CODEC = register("modify_sky_color", Codec.INT.xmap(SkyColorModification::new, SkyColorModification::skyColor).fieldOf("sky_color").codec());

    private static <T extends BiomeModifierAction> Codec<T> register(String path, Codec<T> codec) {
        return BorealibRegistries.BIOME_MODIFIER_ACTION_TYPES.register(Borealib.location(path), codec);
    }

    public static void init() {}

    private record SkyColorModification(int skyColor) implements BiomeModifierAction {

        @Override
        public Codec<SkyColorModification> type() {
            return SKY_COLOR_MODIFICATION_CODEC;
        }

        @Override
        public Stage applicationStage() {
            return Stage.POST_PROCESSING;
        }

        @Override
        public void accept(BiomeInfo.Mutable info) {
            info.getEffectSettings().setSkyColor(this.skyColor);
        }
    }
}

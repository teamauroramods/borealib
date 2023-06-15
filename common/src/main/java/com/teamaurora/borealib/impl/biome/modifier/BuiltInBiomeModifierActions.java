package com.teamaurora.borealib.impl.biome.modifier;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.biome.v1.modifier.BiomeModifierAction;
import com.teamaurora.borealib.api.biome.v1.modifier.info.BiomeInfo;
import com.teamaurora.borealib.api.registry.v1.DeferredRegister;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BuiltInBiomeModifierActions {

    public static final DeferredRegister<Codec<? extends BiomeModifierAction>> WRITER = DeferredRegister.customWriter(BorealibRegistries.BIOME_MODIFIER_ACTION_TYPES, Borealib.MOD_ID);
    private static final RegistryReference<Codec<SkyColorModification>> SKY_COLOR_MODIFICATION_CODEC = WRITER.register("modify_sky_color", () -> Codec.INT.xmap(SkyColorModification::new, SkyColorModification::skyColor).fieldOf("sky_color").codec());

    private record SkyColorModification(int skyColor) implements BiomeModifierAction {


        @Override
        public Codec<SkyColorModification> type() {
            return SKY_COLOR_MODIFICATION_CODEC.get();
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

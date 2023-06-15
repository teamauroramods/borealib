package com.teamaurora.borealib.core.forge;

import com.teamaurora.borealib.api.base.v1.modloading.forge.ForgeModFactory;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.impl.biome.modifier.forge.ForgeBiomeModifierLoader;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Mod(Borealib.MOD_ID)
public class BorealibForge {

    public BorealibForge() {
        ForgeModFactory.loadMod(Borealib.MOD_ID);
        ForgeBiomeModifierLoader.init();
    }
}

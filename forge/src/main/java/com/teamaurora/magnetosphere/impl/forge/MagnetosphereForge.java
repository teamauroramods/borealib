package com.teamaurora.magnetosphere.impl.forge;

import com.teamaurora.magnetosphere.api.base.v1.modloading.forge.ForgeModFactory;
import com.teamaurora.magnetosphere.impl.Magnetosphere;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Mod(Magnetosphere.MOD_ID)
public class MagnetosphereForge {

    public MagnetosphereForge() {
        ForgeModFactory.loadMod(Magnetosphere.MOD_ID);
    }
}

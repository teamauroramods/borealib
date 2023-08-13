package com.teamaurora.borealib.core.forge;

import com.teamaurora.borealib.api.base.v1.util.forge.ForgeHelper;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.BorealibClient;
import com.teamaurora.borealib.core.registry.forge.BorealibForgeBlockEntityTypes;
import com.teamaurora.borealib.impl.biome.modifier.forge.ForgeBiomeModifierLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Mod(Borealib.MOD_ID)
public class BorealibForge {

    public BorealibForge() {
        IEventBus bus = ForgeHelper.getEventBus(Borealib.MOD_ID);
        Borealib.onCommonInit();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> BorealibClient::init);
        bus.<FMLClientSetupEvent>addListener(e -> Borealib.onCommonPostInit(ForgeHelper.createDispatcher(e)));
        ForgeBiomeModifierLoader.init();
        BorealibForgeBlockEntityTypes.init();
    }
}

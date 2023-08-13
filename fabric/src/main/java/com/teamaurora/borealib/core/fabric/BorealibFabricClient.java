package com.teamaurora.borealib.core.fabric;

import com.teamaurora.borealib.core.BorealibClient;
import net.fabricmc.api.ClientModInitializer;

public class BorealibFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BorealibClient.init();
    }
}

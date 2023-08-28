package com.teamaurora.borealib.core.fabric;

import com.teamaurora.borealib.core.BorealibClient;
import com.teamaurora.borealib.core.network.fabric.ConfigSyncHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import java.util.concurrent.CompletableFuture;

public class BorealibFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BorealibClient.init();
        ClientLoginNetworking.registerGlobalReceiver(ConfigSyncHandler.ID, (client, handler, buf, listenerAdder) -> {
            ConfigSyncHandler.read(buf);
            return CompletableFuture.completedFuture(PacketByteBufs.create());
        });
    }
}

package com.teamaurora.borealib.api.network.v1;

import com.teamaurora.borealib.impl.network.NetworkManagerImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface NetworkManager {

    static PlayNetworkChannel createPlayChannel(ResourceLocation channelId, String version, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        return NetworkManagerImpl.createPlayChannel(channelId, version, clientFactory, serverFactory);
    }

    static LoginNetworkChannel createLoginChannel(ResourceLocation channelId, String version, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        return NetworkManagerImpl.createLoginChannel(channelId, version, clientFactory, serverFactory);
    }
}

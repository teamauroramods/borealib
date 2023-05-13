package com.teamaurora.magnetosphere.api.network.v1;

import com.teamaurora.magnetosphere.impl.network.NetworkManagerImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface NetworkManager {

    static PlayNetworkChannel createPlayChannel(ResourceLocation channelId, String version, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        return NetworkManagerImpl.createPlay(channelId, version, clientFactory, serverFactory);
    }

    static LoginNetworkChannel createLoginChannel(ResourceLocation channelId, String version, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        return NetworkManagerImpl.createLogin(channelId, version, clientFactory, serverFactory);
    }
}

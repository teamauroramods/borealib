package com.teamaurora.borealib.impl.network.fabric;

import com.teamaurora.borealib.api.network.v1.LoginNetworkChannel;
import com.teamaurora.borealib.api.network.v1.PlayNetworkChannel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Internal
public class NetworkManagerImplImpl {

    public static PlayNetworkChannel createPlayChannel(ResourceLocation channelId, String version, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        return new FabricPlayChannel(channelId, clientFactory, serverFactory);
    }

    public static LoginNetworkChannel createLoginChannel(ResourceLocation channelId, String version, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        return new FabricLoginChannel(channelId, clientFactory, serverFactory);
    }
}

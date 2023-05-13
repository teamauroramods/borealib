package com.teamaurora.magnetosphere.impl.network.forge;

import com.teamaurora.magnetosphere.api.network.v1.LoginNetworkChannel;
import com.teamaurora.magnetosphere.api.network.v1.PlayNetworkChannel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Internal
public class NetworkManagerImplImpl {
    public static PlayNetworkChannel createPlayChannel(ResourceLocation channelId, String version, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        return new ForgePlayChannel(NetworkRegistry.newSimpleChannel(channelId, () -> version, version::equals, version::equals), clientFactory, serverFactory);
    }

    public static LoginNetworkChannel createLoginChannel(ResourceLocation channelId, String version, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        return new ForgeLoginChannel(NetworkRegistry.newSimpleChannel(channelId, () -> version, version::equals, version::equals), clientFactory, serverFactory);
    }
}

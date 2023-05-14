package com.teamaurora.magnetosphere.core.network;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.api.network.v1.LoginNetworkChannel;
import com.teamaurora.magnetosphere.api.network.v1.NetworkManager;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import com.teamaurora.magnetosphere.core.network.login.MagnetosphereClientLoginPacketHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MagnetosphereMessages {

    public static final LoginNetworkChannel LOGIN = NetworkManager.createLoginChannel(Magnetosphere.location("login"), "1", MagnetosphereMessages::createClientLoginHandler, Object::new);

    public static void init() {

        registerPlatformPackets();
    }

    @ExpectPlatform
    public static MagnetosphereClientLoginPacketHandler createClientLoginHandler() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static void registerPlatformPackets() {
        Platform.expect();
    }
}
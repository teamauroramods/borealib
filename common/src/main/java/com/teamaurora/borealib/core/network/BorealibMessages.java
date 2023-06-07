package com.teamaurora.borealib.core.network;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.network.v1.LoginNetworkChannel;
import com.teamaurora.borealib.api.network.v1.NetworkManager;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.network.login.BorealibClientLoginPacketHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BorealibMessages {

    public static final LoginNetworkChannel LOGIN = NetworkManager.createLoginChannel(Borealib.location("login"), "1", BorealibMessages::createClientLoginHandler, Object::new);

    public static void init() {

        registerPlatformPackets();
    }

    @ExpectPlatform
    public static BorealibClientLoginPacketHandler createClientLoginHandler() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static void registerPlatformPackets() {
        Platform.expect();
    }
}
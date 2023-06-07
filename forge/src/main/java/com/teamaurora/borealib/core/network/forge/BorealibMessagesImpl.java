package com.teamaurora.borealib.core.network.forge;

import com.teamaurora.borealib.core.network.BorealibClientLoginPacketHandlerImpl;
import com.teamaurora.borealib.core.network.login.BorealibClientLoginPacketHandler;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BorealibMessagesImpl {

    public static BorealibClientLoginPacketHandler createClientLoginHandler() {
        return new BorealibClientLoginPacketHandlerImpl();
    }

    public static void registerPlatformPackets() {
    }
}

package com.teamaurora.magnetosphere.core.network.forge;

import com.teamaurora.magnetosphere.core.network.MagnetosphereClientLoginPacketHandlerImpl;
import com.teamaurora.magnetosphere.core.network.login.MagnetosphereClientLoginPacketHandler;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MagnetosphereMessagesImpl {

    public static MagnetosphereClientLoginPacketHandler createClientLoginHandler() {
        return new MagnetosphereClientLoginPacketHandlerImpl();
    }

    public static void registerPlatformPackets() {
    }
}

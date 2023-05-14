package com.teamaurora.magnetosphere.core.network.fabric;

import com.teamaurora.magnetosphere.core.network.MagnetosphereMessages;
import com.teamaurora.magnetosphere.core.network.login.MagnetosphereClientLoginPacketHandler;
import com.teamaurora.magnetosphere.impl.config.fabric.ConfigTracker;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MagnetosphereMessagesImpl {

    public static MagnetosphereClientLoginPacketHandler createClientLoginHandler() {
        return new FabricClientLoginPacketHandlerImpl();
    }

    public static void registerPlatformPackets() {
        MagnetosphereMessages.LOGIN.registerLogin(ClientboundSyncConfigDataPacket.class, ClientboundSyncConfigDataPacket::new, ConfigTracker.INSTANCE::syncConfigs);
    }
}

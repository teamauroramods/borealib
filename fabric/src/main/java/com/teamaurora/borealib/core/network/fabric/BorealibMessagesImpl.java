package com.teamaurora.borealib.core.network.fabric;

import com.teamaurora.borealib.core.network.BorealibMessages;
import com.teamaurora.borealib.core.network.login.BorealibClientLoginPacketHandler;
import com.teamaurora.borealib.impl.config.fabric.ConfigTracker;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BorealibMessagesImpl {

    public static BorealibClientLoginPacketHandler createClientLoginHandler() {
        return new FabricClientLoginPacketHandlerImpl();
    }

    public static void registerPlatformPackets() {
        BorealibMessages.LOGIN.registerLogin(ClientboundSyncConfigDataPacket.class, ClientboundSyncConfigDataPacket::new, ConfigTracker.INSTANCE::syncConfigs);
    }
}

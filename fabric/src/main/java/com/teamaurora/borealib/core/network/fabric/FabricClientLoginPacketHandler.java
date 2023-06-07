package com.teamaurora.borealib.core.network.fabric;

import com.teamaurora.borealib.api.network.v1.message.MagnetospherePacket;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface FabricClientLoginPacketHandler {

    void handleClientboundSyncConfigDataPacket(ClientboundSyncConfigDataPacket msg, MagnetospherePacket.Context ctx);
}

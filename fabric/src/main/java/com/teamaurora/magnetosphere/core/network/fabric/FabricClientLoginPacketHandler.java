package com.teamaurora.magnetosphere.core.network.fabric;

import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface FabricClientLoginPacketHandler {

    void handleClientboundSyncConfigDataPacket(ClientboundSyncConfigDataPacket msg, MagnetospherePacket.Context ctx);
}

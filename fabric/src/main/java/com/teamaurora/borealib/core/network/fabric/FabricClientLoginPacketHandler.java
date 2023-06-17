package com.teamaurora.borealib.core.network.fabric;

import com.teamaurora.borealib.api.network.v1.message.BorealibPacket;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface FabricClientLoginPacketHandler {

    void handleClientboundSyncConfigDataPacket(ClientboundSyncConfigDataPacket msg, BorealibPacket.Context ctx);
}

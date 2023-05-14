package com.teamaurora.magnetosphere.core.network.fabric;

import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import com.teamaurora.magnetosphere.api.network.v1.message.login.ServerboundEmptyResponsePacket;
import com.teamaurora.magnetosphere.core.network.MagnetosphereClientLoginPacketHandlerImpl;
import com.teamaurora.magnetosphere.impl.config.fabric.ConfigTracker;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FabricClientLoginPacketHandlerImpl extends MagnetosphereClientLoginPacketHandlerImpl implements FabricClientLoginPacketHandler {

    @Override
    public void handleClientboundSyncConfigDataPacket(ClientboundSyncConfigDataPacket msg, MagnetospherePacket.Context ctx) {
        ConfigTracker.INSTANCE.receiveSyncedConfig(msg);
        ctx.reply(new ServerboundEmptyResponsePacket());
    }
}

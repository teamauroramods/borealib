package com.teamaurora.borealib.core.network.fabric;

import com.teamaurora.borealib.api.network.v1.message.BorealibPacket;
import com.teamaurora.borealib.api.network.v1.message.login.ServerboundEmptyResponsePacket;
import com.teamaurora.borealib.core.network.BorealibClientLoginPacketHandlerImpl;
import com.teamaurora.borealib.impl.config.fabric.ConfigTracker;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FabricClientLoginPacketHandlerImpl extends BorealibClientLoginPacketHandlerImpl implements FabricClientLoginPacketHandler {

    @Override
    public void handleClientboundSyncConfigDataPacket(ClientboundSyncConfigDataPacket msg, BorealibPacket.Context ctx) {
        ConfigTracker.INSTANCE.receiveSyncedConfig(msg);
        ctx.reply(new ServerboundEmptyResponsePacket());
    }
}

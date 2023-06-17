package com.teamaurora.borealib.impl.network.context.fabric;

import com.teamaurora.borealib.api.network.v1.message.BorealibPacket;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.Connection;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
public class FabricLoginPacketContext extends FabricPacketContext {

    private final Consumer<BorealibPacket<?>> response;
    private boolean replied;

    public FabricLoginPacketContext(Connection connection, Consumer<BorealibPacket<?>> response, ServerLoginNetworking.LoginSynchronizer synchronizer, BorealibPacket.Direction direction) {
        super(connection, synchronizer, direction);
        this.response = response;
    }

    @Override
    public void reply(BorealibPacket<?> packet) {
        if (this.replied)
            throw new IllegalStateException("Cannot reply to login messages twice!");
        this.replied = true;
        this.response.accept(packet);
    }
}
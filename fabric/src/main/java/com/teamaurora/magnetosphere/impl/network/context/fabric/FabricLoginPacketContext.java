package com.teamaurora.magnetosphere.impl.network.context.fabric;

import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
public class FabricLoginPacketContext extends FabricPacketContext {

    private final Consumer<MagnetospherePacket<?>> response;
    private boolean replied;

    public FabricLoginPacketContext(Consumer<MagnetospherePacket<?>> response, ServerLoginNetworking.LoginSynchronizer synchronizer, MagnetospherePacket.Direction direction) {
        super(synchronizer, direction);
        this.response = response;
    }

    @Override
    public void reply(MagnetospherePacket<?> packet) {
        if (this.replied)
            throw new IllegalStateException("Cannot reply to login messages twice!");
        this.replied = true;
        this.response.accept(packet);
    }
}
package com.teamaurora.magnetosphere.impl.network.context.fabric;

import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import net.minecraft.network.Connection;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
public class FabricPlayPacketContext extends FabricPacketContext {

    private final Consumer<MagnetospherePacket<?>> response;

    public FabricPlayPacketContext(Connection connection, Consumer<MagnetospherePacket<?>> response, MagnetospherePacket.Direction direction) {
        super(connection, future -> {
        }, direction);
        this.response = response;
    }

    @Override
    public void reply(MagnetospherePacket<?> packet) {
        this.response.accept(packet);
    }
}
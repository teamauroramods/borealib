package com.teamaurora.borealib.impl.network.context.fabric;

import com.teamaurora.borealib.api.network.v1.message.BorealibPacket;
import net.minecraft.network.Connection;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
public class FabricPlayPacketContext extends FabricPacketContext {

    private final Consumer<BorealibPacket<?>> response;

    public FabricPlayPacketContext(Connection connection, Consumer<BorealibPacket<?>> response, BorealibPacket.Direction direction) {
        super(connection, future -> {
        }, direction);
        this.response = response;
    }

    @Override
    public void reply(BorealibPacket<?> packet) {
        this.response.accept(packet);
    }
}
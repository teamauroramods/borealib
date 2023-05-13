package com.teamaurora.magnetosphere.impl.network.forge;

import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import com.teamaurora.magnetosphere.core.extensions.forge.FMLHandshakeHandlerExtension;
import io.netty.util.AttributeKey;
import net.minecraft.network.Connection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

@ApiStatus.Internal
public class ForgePacketContext implements MagnetospherePacket.Context {

    private final SimpleChannel channel;
    private final Supplier<NetworkEvent.Context> ctx;

    public ForgePacketContext(SimpleChannel channel, Supplier<NetworkEvent.Context> ctx) {
        this.channel = channel;
        this.ctx = ctx;
    }

    @Override
    public CompletableFuture<Void> enqueueWork(Runnable runnable) {
        return this.ctx.get().enqueueWork(runnable);
    }

    @Override
    public void waitFor(Future<?> future) {
        Connection connection = this.ctx.get().getNetworkManager();
        if (connection.getPacketListener() instanceof FMLHandshakeHandlerExtension) {
            ((FMLHandshakeHandlerExtension) connection.channel().attr(AttributeKey.valueOf("fml:handshake")).get()).magnetosphere$addWait(future);
        }
    }

    @Override
    public void reply(MagnetospherePacket<?> packet) {
        this.channel.reply(packet, this.ctx.get());
    }

    @Override
    public MagnetospherePacket.Direction getDirection() {
        return switch (this.ctx.get().getDirection()) {
            case PLAY_TO_SERVER -> MagnetospherePacket.Direction.PLAY_SERVERBOUND;
            case PLAY_TO_CLIENT -> MagnetospherePacket.Direction.PLAY_CLIENTBOUND;
            case LOGIN_TO_SERVER -> MagnetospherePacket.Direction.LOGIN_SERVERBOUND;
            case LOGIN_TO_CLIENT -> MagnetospherePacket.Direction.LOGIN_CLIENTBOUND;
        };
    }
}
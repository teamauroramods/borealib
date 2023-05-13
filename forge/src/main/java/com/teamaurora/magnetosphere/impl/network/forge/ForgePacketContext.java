package com.teamaurora.magnetosphere.impl.network.forge;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import com.teamaurora.magnetosphere.core.extensions.forge.FMLHandshakeHandlerExtension;
import io.netty.util.AttributeKey;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
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
        Connection connection = this.getNetworkManager();
        if (connection.getPacketListener() instanceof FMLHandshakeHandlerExtension) {
            ((FMLHandshakeHandlerExtension) connection.channel().attr(AttributeKey.valueOf("fml:handshake")).get()).magnetosphere$addWait(future);
        }
    }

    @Override
    public void reply(MagnetospherePacket<?> packet) {
        this.channel.reply(packet, this.ctx.get());
    }

    @Override
    public void disconnect(Component message) {
        Connection connection = this.getNetworkManager();
        switch (this.getDirection()) {
            case PLAY_SERVERBOUND -> {
                connection.send(new ClientboundDisconnectPacket(message), new PacketSendListener() {
                    @Override
                    public void onSuccess() {
                        connection.disconnect(message);
                    }
                });
                connection.setReadOnly();
                Platform.getRunningServer().ifPresent(server -> server.executeBlocking(connection::handleDisconnection));
            }
            case LOGIN_SERVERBOUND -> {
                connection.send(new ClientboundLoginDisconnectPacket(message), new PacketSendListener() {
                    @Override
                    public void onSuccess() {
                        connection.disconnect(message);
                    }
                });
                connection.setReadOnly();
                Platform.getRunningServer().ifPresent(server -> server.executeBlocking(connection::handleDisconnection));
            }
            case PLAY_CLIENTBOUND, LOGIN_CLIENTBOUND -> connection.disconnect(message);
        }
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

    @Override
    public Connection getNetworkManager() {
        return this.ctx.get().getNetworkManager();
    }
}
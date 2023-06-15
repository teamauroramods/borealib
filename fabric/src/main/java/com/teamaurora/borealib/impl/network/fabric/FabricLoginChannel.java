package com.teamaurora.borealib.impl.network.fabric;

import com.teamaurora.borealib.api.network.v1.LoginNetworkChannel;
import com.teamaurora.borealib.api.network.v1.message.MagnetospherePacket;
import com.teamaurora.borealib.api.network.v1.message.PacketDecoder;
import com.teamaurora.borealib.api.network.v1.message.login.MagnetosphereLoginPacket;
import com.teamaurora.borealib.core.extensions.fabric.ServerLoginPacketListenerImplExtension;
import com.teamaurora.borealib.core.mixin.fabric.ClientHandshakePacketListenerImplAccessor;
import com.teamaurora.borealib.impl.network.NetworkManagerImpl;
import com.teamaurora.borealib.impl.network.context.fabric.FabricLoginPacketContext;
import com.teamaurora.borealib.impl.network.context.fabric.FabricPacketContext;
import com.teamaurora.borealib.core.mixin.fabric.ServerLoginPacketListenerImplAccessor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.Internal
public class FabricLoginChannel extends NetworkChannelImpl implements LoginNetworkChannel {

    private final List<Function<Boolean, ? extends List<? extends Pair<String, ?>>>> loginPackets;

    public FabricLoginChannel(ResourceLocation channelId, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        super(channelId, clientFactory, serverFactory);
        this.loginPackets = new ArrayList<>();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientLoginNetworking.registerGlobalReceiver(this.channelId, this::processClient);
            ClientPlayNetworking.registerGlobalReceiver(this.channelId, (client, handler, buf, responseSender) -> this.processClient(client, handler, buf, null));
        }
        ServerLoginNetworking.registerGlobalReceiver(this.channelId, this::processServer);
        ServerPlayNetworking.registerGlobalReceiver(this.channelId, (server, player, handler, buf, responseSender) -> this.processServer(server, handler, true, buf, future -> {
            // Don't wait because the player is already being placed into the level
        }, responseSender));

        ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> this.loginPackets.stream().flatMap(function -> function.apply(((ServerLoginPacketListenerImplAccessor) handler).getConnection().isMemoryConnection()).stream()).forEach(pair -> {
            Packet<?> packet = sender.createPacket(this.channelId, this.serialize((MagnetospherePacket<?>) pair.getValue(), MagnetospherePacket.Direction.LOGIN_CLIENTBOUND));
            if (packet instanceof ClientboundCustomQueryPacket) {
                ((ServerLoginPacketListenerImplExtension) handler).borealib$trackPacket((ClientboundCustomQueryPacket) packet);
            }
            sender.sendPacket(packet);
        }));
    }

    private CompletableFuture<@Nullable FriendlyByteBuf> processClient(Minecraft minecraft, ClientPacketListener listener, FriendlyByteBuf data, @Nullable Consumer<GenericFutureListener<? extends Future<? super Void>>> listenerAdder) {
        CompletableFuture<FriendlyByteBuf> future = new CompletableFuture<>();
        NetworkManagerImpl.processMessage(this.deserialize(data, MagnetospherePacket.Direction.LOGIN_CLIENTBOUND), new FabricLoginPacketContext(((ClientHandshakePacketListenerImplAccessor)listener).getConnection(), pkt -> {
            try {
                future.complete(this.serialize(pkt, MagnetospherePacket.Direction.LOGIN_SERVERBOUND));
            } catch (Throwable t) {
                t.printStackTrace();
                future.completeExceptionally(t);
            }
        }, __ -> {}, MagnetospherePacket.Direction.LOGIN_CLIENTBOUND), this.clientMessageHandler);
        return future;
    }

    private CompletableFuture<@Nullable FriendlyByteBuf> processClient(Minecraft minecraft, ClientHandshakePacketListenerImpl listener, FriendlyByteBuf data, @Nullable Consumer<GenericFutureListener<? extends Future<? super Void>>> listenerAdder) {
        CompletableFuture<FriendlyByteBuf> future = new CompletableFuture<>();
        NetworkManagerImpl.processMessage(this.deserialize(data, MagnetospherePacket.Direction.LOGIN_CLIENTBOUND), new FabricLoginPacketContext(((ClientHandshakePacketListenerImplAccessor)listener).getConnection(), pkt -> {
            try {
                future.complete(this.serialize(pkt, MagnetospherePacket.Direction.LOGIN_SERVERBOUND));
            } catch (Throwable t) {
                t.printStackTrace();
                future.completeExceptionally(t);
            }
        }, __ -> {}, MagnetospherePacket.Direction.LOGIN_CLIENTBOUND), this.clientMessageHandler);
        return future;
    }

    private void processServer(MinecraftServer server, PacketListener listener, boolean understood, FriendlyByteBuf data, ServerLoginNetworking.LoginSynchronizer synchronizer, PacketSender responseSender) {
        if (!understood) {
            if (!(listener instanceof ServerLoginPacketListenerImplExtension))
                throw new IllegalStateException("Client failed to process server message");
            ((ServerLoginPacketListenerImplExtension) listener).borealib$delayPacket();
            return;
        }

        NetworkManagerImpl.processMessage(this.deserialize(data, MagnetospherePacket.Direction.LOGIN_SERVERBOUND), new FabricPacketContext(((ServerLoginPacketListenerImplAccessor) listener).getConnection(),synchronizer, MagnetospherePacket.Direction.LOGIN_SERVERBOUND) {
            @Override
            public void reply(MagnetospherePacket<?> packet) {
                throw new UnsupportedOperationException("The server is not allowed to reply during the login phase");
            }
        }, this.serverMessageHandler);
    }

    @Override
    public <MSG extends MagnetosphereLoginPacket<T>, T> void register(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer) {
        super.register(clazz, deserializer, MagnetospherePacket.Direction.LOGIN_SERVERBOUND);
    }

    @Override
    public <MSG extends MagnetosphereLoginPacket<T>, T> void registerLogin(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer, Function<Boolean, List<Pair<String, MSG>>> loginMessageGenerators) {
        super.register(clazz, deserializer, MagnetospherePacket.Direction.LOGIN_CLIENTBOUND);
        this.loginPackets.add(loginMessageGenerators);
    }

    @Override
    public Packet<?> toVanillaPacket(MagnetospherePacket<?> packet, int transactionId, MagnetospherePacket.Direction direction) {
        return switch (direction) {
            case LOGIN_SERVERBOUND ->
                    new ServerboundCustomQueryPacket(transactionId, this.serialize(packet, direction));
            case LOGIN_CLIENTBOUND ->
                    new ClientboundCustomQueryPacket(transactionId, this.channelId, this.serialize(packet, direction));
            default -> throw new IllegalStateException("Unsupported packet direction: " + direction);
        };
    }
}
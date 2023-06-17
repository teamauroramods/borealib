package com.teamaurora.borealib.impl.network.fabric;

import com.teamaurora.borealib.api.network.v1.PlayNetworkChannel;
import com.teamaurora.borealib.api.network.v1.message.BorealibPacket;
import com.teamaurora.borealib.api.network.v1.message.PacketDecoder;
import com.teamaurora.borealib.core.mixin.fabric.ServerGamePacketListenerImplAccessor;
import com.teamaurora.borealib.impl.network.NetworkManagerImpl;
import com.teamaurora.borealib.impl.network.context.fabric.FabricPlayPacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@ApiStatus.Internal
public class FabricPlayChannel extends NetworkChannelImpl implements PlayNetworkChannel {

    public FabricPlayChannel(ResourceLocation channelId, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        super(channelId, clientFactory, serverFactory);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(this.channelId, this::processClientPlay);
        }
        ServerPlayNetworking.registerGlobalReceiver(this.channelId, this::processServerPlay);
    }

    private void processClientPlay(Minecraft client, ClientPacketListener listener, FriendlyByteBuf data, PacketSender responseSender) {
        NetworkManagerImpl.processMessage(this.deserialize(data, BorealibPacket.Direction.PLAY_CLIENTBOUND), new FabricPlayPacketContext(listener.getConnection(), msg -> responseSender.sendPacket(responseSender.createPacket(this.channelId, this.serialize(msg, BorealibPacket.Direction.PLAY_SERVERBOUND))), BorealibPacket.Direction.PLAY_CLIENTBOUND), this.clientMessageHandler);
    }

    private void processServerPlay(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf data, PacketSender responseSender) {
        NetworkManagerImpl.processMessage(this.deserialize(data, BorealibPacket.Direction.PLAY_SERVERBOUND), new FabricPlayPacketContext(((ServerGamePacketListenerImplAccessor) listener).getConnection(), pkt -> responseSender.sendPacket(responseSender.createPacket(this.channelId, this.serialize(pkt, BorealibPacket.Direction.PLAY_CLIENTBOUND))), BorealibPacket.Direction.PLAY_SERVERBOUND), this.serverMessageHandler);
    }

    @Override
    public void sendTo(ServerPlayer player, BorealibPacket<?> packet) {
        ServerPlayNetworking.send(player, this.channelId, this.serialize(packet, BorealibPacket.Direction.PLAY_CLIENTBOUND));
    }

    @Override
    public void sendTo(ServerLevel level, BorealibPacket<?> packet) {
        FriendlyByteBuf data = this.serialize(packet, BorealibPacket.Direction.PLAY_CLIENTBOUND);
        for (ServerPlayer player : PlayerLookup.world(level)) {
            ServerPlayNetworking.send(player, this.channelId, data);
        }
    }

    @Override
    public void sendToNear(ServerLevel level, double x, double y, double z, double radius, BorealibPacket<?> packet) {
        FriendlyByteBuf data = this.serialize(packet, BorealibPacket.Direction.PLAY_CLIENTBOUND);
        for (ServerPlayer player : PlayerLookup.around(level, new Vec3(x, y, z), radius)) {
            ServerPlayNetworking.send(player, this.channelId, data);
        }
    }

    @Override
    public void sendToAll(MinecraftServer server, BorealibPacket<?> packet) {
        FriendlyByteBuf data = this.serialize(packet, BorealibPacket.Direction.PLAY_CLIENTBOUND);
        for (ServerPlayer player : PlayerLookup.all(server)) {
            ServerPlayNetworking.send(player, this.channelId, data);
        }
    }

    @Override
    public void sendToTracking(Entity entity, BorealibPacket<?> packet) {
        FriendlyByteBuf data = this.serialize(packet, BorealibPacket.Direction.PLAY_CLIENTBOUND);
        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, this.channelId, data);
        }
    }

    @Override
    public void sendToTracking(ServerLevel level, BlockPos pos, BorealibPacket<?> packet) {
        FriendlyByteBuf data = this.serialize(packet, BorealibPacket.Direction.PLAY_CLIENTBOUND);
        for (ServerPlayer player : PlayerLookup.tracking(level, pos)) {
            ServerPlayNetworking.send(player, this.channelId, data);
        }
    }

    @Override
    public void sendToTracking(ServerLevel level, ChunkPos pos, BorealibPacket<?> packet) {
        FriendlyByteBuf data = this.serialize(packet, BorealibPacket.Direction.PLAY_CLIENTBOUND);
        for (ServerPlayer player : PlayerLookup.tracking(level, pos)) {
            ServerPlayNetworking.send(player, this.channelId, data);
        }
    }

    @Override
    public void sendToTrackingAndSelf(Entity entity, BorealibPacket<?> packet) {
        FriendlyByteBuf data = this.serialize(packet, BorealibPacket.Direction.PLAY_CLIENTBOUND);
        if (entity instanceof ServerPlayer player) {
            ServerPlayNetworking.send(player, this.channelId, data);
        }
        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, this.channelId, data);
        }
    }

    @Override
    public void sendToServer(BorealibPacket<?> packet) {
        ClientPlayNetworking.send(this.channelId, this.serialize(packet, BorealibPacket.Direction.PLAY_SERVERBOUND));
    }

    @Override
    public Packet<?> toVanillaPacket(BorealibPacket<?> packet, BorealibPacket.Direction direction) {
        return switch (direction) {
            case PLAY_SERVERBOUND ->
                    new ServerboundCustomPayloadPacket(this.channelId, this.serialize(packet, direction));
            case PLAY_CLIENTBOUND ->
                    new ClientboundCustomPayloadPacket(this.channelId, this.serialize(packet, direction));
            default -> throw new IllegalStateException("Unsupported message direction: " + direction);
        };
    }

    @Override
    public <MSG extends BorealibPacket<T>, T> void register(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer, @Nullable BorealibPacket.Direction direction) {
        super.register(clazz, deserializer, direction);
    }
}
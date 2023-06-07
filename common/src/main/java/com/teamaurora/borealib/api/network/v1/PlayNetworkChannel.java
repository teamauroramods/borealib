package com.teamaurora.borealib.api.network.v1;

import com.teamaurora.borealib.api.network.v1.message.MagnetospherePacket;
import com.teamaurora.borealib.api.network.v1.message.PacketDecoder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

public interface PlayNetworkChannel {

    void sendTo(ServerPlayer player, MagnetospherePacket<?> packet);

    void sendTo(ServerLevel level, MagnetospherePacket<?> packet);

    void sendToNear(ServerLevel level, double x, double y, double z, double radius, MagnetospherePacket<?> packet);

    void sendToAll(MinecraftServer server, MagnetospherePacket<?> packet);

    void sendToTracking(Entity entity, MagnetospherePacket<?> packet);

    void sendToTracking(ServerLevel level, BlockPos pos, MagnetospherePacket<?> packet);

    default void sendToTracking(LevelChunk chunk, MagnetospherePacket<?> packet) {
        this.sendToTracking((ServerLevel) chunk.getLevel(), chunk.getPos(), packet);
    }

    void sendToTracking(ServerLevel level, ChunkPos pos, MagnetospherePacket<?> packet);

    void sendToTrackingAndSelf(Entity entity, MagnetospherePacket<?> packet);

    void sendToServer(MagnetospherePacket<?> packet);

    <MSG extends MagnetospherePacket<T>, T> void register(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer, @Nullable MagnetospherePacket.Direction direction);

    Packet<?> toVanillaPacket(MagnetospherePacket<?> packet, MagnetospherePacket.Direction direction);
}

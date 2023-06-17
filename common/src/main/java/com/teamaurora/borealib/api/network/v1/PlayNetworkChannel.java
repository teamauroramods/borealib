package com.teamaurora.borealib.api.network.v1;

import com.teamaurora.borealib.api.network.v1.message.BorealibPacket;
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

    void sendTo(ServerPlayer player, BorealibPacket<?> packet);

    void sendTo(ServerLevel level, BorealibPacket<?> packet);

    void sendToNear(ServerLevel level, double x, double y, double z, double radius, BorealibPacket<?> packet);

    void sendToAll(MinecraftServer server, BorealibPacket<?> packet);

    void sendToTracking(Entity entity, BorealibPacket<?> packet);

    void sendToTracking(ServerLevel level, BlockPos pos, BorealibPacket<?> packet);

    default void sendToTracking(LevelChunk chunk, BorealibPacket<?> packet) {
        this.sendToTracking((ServerLevel) chunk.getLevel(), chunk.getPos(), packet);
    }

    void sendToTracking(ServerLevel level, ChunkPos pos, BorealibPacket<?> packet);

    void sendToTrackingAndSelf(Entity entity, BorealibPacket<?> packet);

    void sendToServer(BorealibPacket<?> packet);

    <MSG extends BorealibPacket<T>, T> void register(Class<MSG> clazz, PacketDecoder<MSG, T> deserializer, @Nullable BorealibPacket.Direction direction);

    Packet<?> toVanillaPacket(BorealibPacket<?> packet, BorealibPacket.Direction direction);
}

package com.teamaurora.borealib.impl.network.forge;

import com.teamaurora.borealib.api.network.v1.PlayNetworkChannel;
import com.teamaurora.borealib.api.network.v1.message.MagnetospherePacket;
import com.teamaurora.borealib.api.network.v1.message.PacketDecoder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@ApiStatus.Internal
public class ForgePlayChannel extends NetworkChannelImpl implements PlayNetworkChannel {

    public ForgePlayChannel(SimpleChannel channel, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        super(channel, clientFactory, serverFactory);
    }

    @Override
    public void sendTo(ServerPlayer player, MagnetospherePacket<?> message) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    @Override
    public void sendTo(ServerLevel level, MagnetospherePacket<?> message) {
        this.channel.send(PacketDistributor.DIMENSION.with(level::dimension), message);
    }

    @Override
    public void sendToNear(ServerLevel level, double x, double y, double z, double radius, MagnetospherePacket<?> message) {
        this.channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(x, y, z, radius * radius, level.dimension())), message);
    }

    @Override
    public void sendToAll(MinecraftServer server, MagnetospherePacket<?> message) {
        this.channel.send(PacketDistributor.ALL.noArg(), message);
    }

    @Override
    public void sendToTracking(Entity entity, MagnetospherePacket<?> message) {
        this.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    @Override
    public void sendToTracking(ServerLevel level, BlockPos pos, MagnetospherePacket<?> message) {
        this.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), message);
    }

    @Override
    public void sendToTracking(ServerLevel level, ChunkPos pos, MagnetospherePacket<?> message) {
        this.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunk(pos.x, pos.z)), message);
    }

    @Override
    public void sendToTrackingAndSelf(Entity entity, MagnetospherePacket<?> message) {
        this.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void sendToServer(MagnetospherePacket<?> message) {
        this.channel.sendToServer(message);
    }

    @Override
    public <MSG extends MagnetospherePacket<T>, T> void register(Class<MSG> clazz, PacketDecoder<MSG, T> decoder, @Nullable MagnetospherePacket.Direction direction) {
        this.getMessageBuilder(clazz, decoder, direction).add();
    }

    @Override
    public Packet<?> toVanillaPacket(MagnetospherePacket<?> packet, MagnetospherePacket.Direction direction) {
        return this.channel.toVanillaPacket(packet, convert(direction));
    }
}
package com.teamaurora.magnetosphere.impl.network;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.api.network.v1.LoginNetworkChannel;
import com.teamaurora.magnetosphere.api.network.v1.PlayNetworkChannel;
import com.teamaurora.magnetosphere.api.network.v1.message.MagnetospherePacket;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatusPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@ApiStatus.Internal
public final class NetworkManagerImpl {

    @ExpectPlatform
    public static PlayNetworkChannel createPlayChannel(ResourceLocation channelId, String version, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static LoginNetworkChannel createLoginChannel(ResourceLocation channelId, String version, Supplier<Object> clientFactory, Supplier<Object> serverFactory) {
        return Platform.expect();
    }

    @SuppressWarnings("unchecked")
    public static <MSG extends MagnetospherePacket<T>, T> void processMessage(@NotNull MSG msg, MagnetospherePacket.Context context, Object handler) {
        try {
            msg.read((T) handler, context);
        } catch (Exception e) {
            LogManager.getLogger().error("Failed to process packet for class: " + msg.getClass().getName(), e);
            Component reason = Component.translatable("disconnect.genericReason", "Internal Exception: " + e);
            Connection networkManager = context.getNetworkManager();
            PacketListener netHandler = networkManager.getPacketListener();
            if (netHandler instanceof ServerStatusPacketListener) {
                networkManager.disconnect(reason);
            } else if (netHandler instanceof ServerLoginPacketListenerImpl) {
                ((ServerLoginPacketListenerImpl) netHandler).disconnect(reason);
            } else if (netHandler instanceof ServerGamePacketListenerImpl) {
                ((ServerGamePacketListenerImpl) netHandler).disconnect(reason);
            } else {
                networkManager.disconnect(reason);
                netHandler.onDisconnect(reason);
            }
        }
    }
}
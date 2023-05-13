package com.teamaurora.magnetosphere.core.extensions.fabric;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public interface ServerLoginPacketListenerImplExtension {

    Connection magnetosphere$getConnection();

    void magnetosphere$trackPacket(ClientboundCustomQueryPacket packet);

    void magnetosphere$delayPacket();

    void magnetosphere$flushDelayedPackets(ServerGamePacketListenerImpl listener);
}
package com.teamaurora.borealib.core.extensions.fabric;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public interface ServerLoginPacketListenerImplExtension {

    Connection borealib$getConnection();

    void borealib$trackPacket(ClientboundCustomQueryPacket packet);

    void borealib$delayPacket();

    void borealib$flushDelayedPackets(ServerGamePacketListenerImpl listener);
}
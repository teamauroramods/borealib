package com.teamaurora.magnetosphere.core.mixin.fabric;

import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientHandshakePacketListenerImpl.class)
public interface ClientHandshakePacketListenerImplAccessor {

    @Accessor("connection")
    Connection getConnection();
}

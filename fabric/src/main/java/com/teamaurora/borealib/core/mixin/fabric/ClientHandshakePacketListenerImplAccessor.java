package com.teamaurora.borealib.core.mixin.fabric;

import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientHandshakePacketListenerImpl.class)
public interface ClientHandshakePacketListenerImplAccessor {

    @Accessor("connection")
    Connection getConnection();
}

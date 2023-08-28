package com.teamaurora.borealib.core.network.fabric;

import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.impl.config.fabric.ConfigTracker;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ConfigSyncHandler {

    public static final ResourceLocation ID = Borealib.location("config_sync");

    public static FriendlyByteBuf write(String filename, byte[] fileData) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeUtf(filename);
        buf.writeByteArray(fileData);
        return buf;
    }

    public static void read(FriendlyByteBuf buf) {
        ConfigTracker.INSTANCE.receiveSyncedConfig(buf.readUtf(), buf.readByteArray());
    }
}
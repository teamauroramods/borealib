package com.teamaurora.borealib.core.network.fabric;

import com.teamaurora.borealib.api.network.v1.message.login.SimpleMagnetosphereLoginPacket;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ClientboundSyncConfigDataPacket extends SimpleMagnetosphereLoginPacket<FabricClientLoginPacketHandler> {

    private final String fileName;
    private final byte[] fileData;

    public ClientboundSyncConfigDataPacket(String fileName, byte[] fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public ClientboundSyncConfigDataPacket(FriendlyByteBuf buf) {
        this.fileName = buf.readUtf();
        this.fileData = buf.readByteArray();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.fileName);
        buf.writeByteArray(this.fileData);
    }

    @Override
    public void read(FabricClientLoginPacketHandler handler, Context ctx) {
        handler.handleClientboundSyncConfigDataPacket(this, ctx);
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }
}
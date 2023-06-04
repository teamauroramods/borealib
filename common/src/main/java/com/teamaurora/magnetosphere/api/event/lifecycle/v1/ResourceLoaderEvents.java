package com.teamaurora.magnetosphere.api.event.lifecycle.v1;

import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;

public final class ResourceLoaderEvents {

    public static final Event<EndDataPackReload> END_DATA_PACK_RELOAD = Event.createLoop(EndDataPackReload.class);

    @FunctionalInterface
    public interface EndDataPackReload {
        void onEndDataPackReload(@Nullable MinecraftServer server, ResourceManager resourceManager, @Nullable Throwable error);
    }

    private ResourceLoaderEvents() {
    }
}

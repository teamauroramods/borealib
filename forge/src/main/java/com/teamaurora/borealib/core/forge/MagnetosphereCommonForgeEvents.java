package com.teamaurora.magnetosphere.core.forge;

import com.teamaurora.magnetosphere.api.event.lifecycle.v1.ServerLifecycleEvents;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import net.minecraftforge.event.server.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Mod.EventBusSubscriber(modid = Magnetosphere.MOD_ID)
public class MagnetosphereCommonForgeEvents {

    @SubscribeEvent
    public static void onEvent(ServerAboutToStartEvent event) {
        if (!ServerLifecycleEvents.PRE_STARTING.invoker().forServer(event.getServer()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(ServerStartingEvent event) {
        if (!ServerLifecycleEvents.STARTING.invoker().forServer(event.getServer()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(ServerStartedEvent event) {
        ServerLifecycleEvents.STARTED.invoker().forServer(event.getServer());
    }

    @SubscribeEvent
    public static void onEvent(ServerStoppingEvent event) {
        ServerLifecycleEvents.STOPPING.invoker().forServer(event.getServer());
    }

    @SubscribeEvent
    public static void onEvent(ServerStoppedEvent event) {
        ServerLifecycleEvents.STOPPED.invoker().forServer(event.getServer());
    }
}
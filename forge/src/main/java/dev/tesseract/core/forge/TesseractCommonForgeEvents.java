package dev.tesseract.core.forge;

import dev.tesseract.api.event.events.lifecycle.ServerLifecycleEvents;
import dev.tesseract.core.Tesseract;
import net.minecraftforge.event.server.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Mod.EventBusSubscriber(modid = Tesseract.MOD_ID)
public class TesseractCommonForgeEvents {

    @SubscribeEvent
    public static void onEvent(ServerAboutToStartEvent event) {
        if (!ServerLifecycleEvents.PRE_STARTING.invoker().preStarting(event.getServer()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(ServerStartingEvent event) {
        if (!ServerLifecycleEvents.STARTING.invoker().starting(event.getServer()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEvent(ServerStartedEvent event) {
        ServerLifecycleEvents.STARTED.invoker().started(event.getServer());
    }

    @SubscribeEvent
    public static void onEvent(ServerStoppingEvent event) {
        ServerLifecycleEvents.STOPPING.invoker().stopping(event.getServer());
    }

    @SubscribeEvent
    public static void onEvent(ServerStoppedEvent event) {
        ServerLifecycleEvents.STOPPED.invoker().stopped(event.getServer());
    }
}

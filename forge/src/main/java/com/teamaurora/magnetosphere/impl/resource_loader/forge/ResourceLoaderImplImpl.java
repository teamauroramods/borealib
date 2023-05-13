package com.teamaurora.magnetosphere.impl.resource_loader.forge;

import com.mojang.datafixers.util.Pair;
import com.teamaurora.magnetosphere.api.resource_loader.v1.NamedReloadListener;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import com.teamaurora.magnetosphere.impl.resource_loader.ResourceLoaderImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

@ApiStatus.Internal
@Mod.EventBusSubscriber(modid = Magnetosphere.MOD_ID)
public class ResourceLoaderImplImpl extends ResourceLoaderImpl {

    private final Set<NamedReloadListener> listeners = new HashSet<>();
    private static final EnumMap<PackType, ResourceLoaderImplImpl> TRACKER = new EnumMap<>(PackType.class);

    public static ResourceLoaderImpl get(PackType packType) {
        return TRACKER.computeIfAbsent(packType, __ -> new ResourceLoaderImplImpl());
    }

    @Override
    public void registerReloadListener(NamedReloadListener reloadListener) {
        if (!this.listeners.add(reloadListener))
            throw new RuntimeException("Duplicate reloader registration: " + reloadListener.getName());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEvent(AddReloadListenerEvent event) {
        addReloadersInOrder(PackType.SERVER_DATA, event.getListeners()).forEach(event::addListener);
    }

    private static List<PreparableReloadListener> addReloadersInOrder(PackType type, List<PreparableReloadListener> existing) {
        ResourceLoaderImplImpl loader = TRACKER.get(type);
        if (loader == null)
            return Collections.emptyList();
        Set<NamedReloadListener> unorderedListeners = TRACKER.get(type).listeners;
        List<NamedReloadListener> toAdd = new ArrayList<>(unorderedListeners);
        Set<ResourceLocation> resolvedIds = new HashSet<>();
        existing.forEach(listener -> {
            if (listener instanceof NamedReloadListener namedReloadListener)
                resolvedIds.add(namedReloadListener.getId());
        });
        int lastSize = -1;
        List<PreparableReloadListener> listeners = new ArrayList<>();
        while (listeners.size() != lastSize) {
            lastSize = listeners.size();
            Iterator<NamedReloadListener> iterator = toAdd.iterator();
            while (iterator.hasNext()) {
                NamedReloadListener listener = iterator.next();
                if (resolvedIds.containsAll(loader.ordering.stream().filter(pair -> pair.getSecond() == listener.getId()).map(Pair::getFirst).toList())) {
                    resolvedIds.add(listener.getId());
                    listeners.add(listener);
                    iterator.remove();
                }
            }
        }
        toAdd.forEach(l -> LogManager.getLogger().warn("Could not locate dependencies for listener " + l.getName()));
        return listeners;
    }

    @Mod.EventBusSubscriber(modid = Magnetosphere.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientReloaderHandler {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onEvent(RegisterClientReloadListenersEvent event) {
            addReloadersInOrder(PackType.CLIENT_RESOURCES, Collections.emptyList()).forEach(event::registerReloadListener);
        }
    }
}

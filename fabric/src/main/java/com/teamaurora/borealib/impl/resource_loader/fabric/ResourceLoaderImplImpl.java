package com.teamaurora.borealib.impl.resource_loader.fabric;

import com.mojang.datafixers.util.Pair;
import com.teamaurora.borealib.api.resource_loader.v1.NamedReloadListener;
import com.teamaurora.borealib.impl.resource_loader.ResourceLoaderImpl;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collection;
import java.util.EnumMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class ResourceLoaderImplImpl extends ResourceLoaderImpl {

    private final ResourceManagerHelper parent;
    private static final EnumMap<PackType, ResourceLoaderImplImpl> TRACKER = new EnumMap<>(PackType.class);

    private ResourceLoaderImplImpl(ResourceManagerHelper parent) {
        this.parent = parent;
    }

    public static ResourceLoaderImpl get(PackType packType) {
        return TRACKER.computeIfAbsent(packType, t -> new ResourceLoaderImplImpl(ResourceManagerHelper.get(t)));
    }

    @Override
    public void registerReloadListener(NamedReloadListener reloadListener) {
        this.parent.registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return reloadListener.getId();
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
                return reloadListener.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
            }

            @Override
            public Collection<ResourceLocation> getFabricDependencies() {
                return ordering.stream().filter(pair -> pair.getSecond() == getFabricId()).map(Pair::getFirst).collect(Collectors.toList());
            }
        });
    }
}

package com.teamaurora.magnetosphere.impl.content_registries;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.teamaurora.magnetosphere.api.content_registries.v1.ContentRegistry;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import com.teamaurora.magnetosphere.api.resource_loader.v1.SimpleStackedJsonResourceReloadListener;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ContentRegistriesImpl extends SimpleStackedJsonResourceReloadListener {

    private static final Map<ResourceLocation, ContentRegistryImpl<?, ?>> KNOWN_REGISTRIES = new ConcurrentHashMap<>();
    private static final ResourceLocation NAME = Magnetosphere.location("content_registries");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    protected ContentRegistriesImpl() {
        super(GSON, "content_registries");
    }

    @Override
    public ResourceLocation getId() {
        return NAME;
    }

    @Override
    public List<String> createStackPaths() {
        return RegistryView.allRegistries()
                .stream()
                .map(Map.Entry::getKey)
                .map(location -> location.getNamespace().equals("minecraft") ? location.getPath() : location.getNamespace() + "/" + location.getPath())
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    protected void apply(Map<ResourceLocation, List<JsonElement>> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        map.forEach((location, jsonElements) -> {
            // Resource location is formatted to <modid>:<path> which should be the registry name
            ContentRegistryImpl<?, ?> registry = KNOWN_REGISTRIES.get(location);
            if (registry != null) {
                registry.byValue.clear();
                registry.byTag.clear();
                Map<ContentRegistryFile.KeyEntry, Object> toAdd = new HashMap<>();
                jsonElements.forEach(json -> {
                    ContentRegistryFile<?> file = ContentRegistryFile.codec(registry.elementCodec()).parse(JsonOps.INSTANCE, json).getOrThrow(false, Magnetosphere.LOGGER::error);
                    if (file.replace())
                        toAdd.clear();
                    file.values().forEach(entry -> toAdd.put(entry.entry(), entry.object()));
                });
                toAdd.forEach((key, object) -> {
                    if (key.tag()) {

                    }
                });
            } else {
                Magnetosphere.LOGGER.error("Unknown content registry " + location + ", ignoring");
            }
        });
    }
}

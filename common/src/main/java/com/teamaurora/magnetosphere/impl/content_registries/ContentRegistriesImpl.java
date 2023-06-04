package com.teamaurora.magnetosphere.impl.content_registries;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.teamaurora.magnetosphere.api.content_registries.v1.ContentRegistries;
import com.teamaurora.magnetosphere.api.content_registries.v1.ContentRegistry;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import com.teamaurora.magnetosphere.api.resource_loader.v1.ReloadListenerKeys;
import com.teamaurora.magnetosphere.api.resource_loader.v1.ResourceLoader;
import com.teamaurora.magnetosphere.api.resource_loader.v1.SimpleStackedJsonResourceReloadListener;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@ApiStatus.Internal
public class ContentRegistriesImpl extends SimpleStackedJsonResourceReloadListener {

    private static final Map<ResourceLocation, ContentRegistryImpl<?, ?>> KNOWN_REGISTRIES = new ConcurrentHashMap<>();
    private static final ResourceLocation NAME = Magnetosphere.location("content_registries");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final ContentRegistriesImpl INSTANCE = new ContentRegistriesImpl();

    private ContentRegistriesImpl() {
        super(GSON, "content_registries");
    }

    @Override
    public ResourceLocation getId() {
        return NAME;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T, R> ContentRegistry<T, R> get(ResourceLocation registryId) {
        return (ContentRegistry<T, R>) KNOWN_REGISTRIES.get(registryId);
    }

    public static <T, R> ContentRegistry<T, R> create(ResourceLocation registryId, RegistryView<T> parentRegistry, Codec<R> elementCodec) {
        if (KNOWN_REGISTRIES.containsKey(registryId)) throw new IllegalStateException("Duplicate content registry " + registryId);
        ContentRegistryImpl<T, R> registry = new ContentRegistryImpl<>(registryId, parentRegistry, elementCodec);
        KNOWN_REGISTRIES.put(registryId, registry);
        return registry;
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

            ContentRegistryImpl<?, ?> registry = KNOWN_REGISTRIES.get(location);
            if (registry != null) {
                registry.reload(jsonElements);
            } else {
                Magnetosphere.LOGGER.error("Unknown content registry " + location + ", ignoring");
            }
        });
    }

    public static void init() {
        ResourceLoader resourceLoader = ResourceLoader.get(PackType.SERVER_DATA);
        resourceLoader.addReloaderOrdering(ReloadListenerKeys.TAGS, NAME);
        resourceLoader.registerReloadListener(INSTANCE);
    }
}

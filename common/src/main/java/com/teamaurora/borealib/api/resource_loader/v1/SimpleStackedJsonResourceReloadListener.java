package com.teamaurora.borealib.api.resource_loader.v1;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionRegistry;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SimpleStackedJsonResourceReloadListener extends SimplePreparableReloadListener<Map<ResourceLocation, List<JsonElement>>> implements NamedReloadListener {

    private final Gson gson;
    private final String directory;

    protected SimpleStackedJsonResourceReloadListener(Gson gson, String directory) {
        this.gson = gson;
        this.directory = directory;
    }

    /**
     * This method creates a list of resource stacks to look for.
     *
     * @return A list of resource paths to find stacks for during the preparation phase
     */
    public abstract List<String> createStackPaths();

    @Override
    protected Map<ResourceLocation, List<JsonElement>> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, List<JsonElement>> map = new HashMap<>();
        for (String path : this.createStackPaths()) {
            FileToIdConverter converter = FileToIdConverter.json(this.directory + "/" + path);
            Map<ResourceLocation, List<Resource>> resources = converter.listMatchingResourceStacks(resourceManager);
            if (resources.isEmpty()) continue;
            for (Map.Entry<ResourceLocation, List<Resource>> resourceEntry : resources.entrySet()) {
                ResourceLocation rawId = resourceEntry.getKey();
                ResourceLocation idToUse = converter.fileToId(rawId);
                List<JsonElement> list = map.computeIfAbsent(idToUse, __ -> new ArrayList<>());
                for (Resource resource : resourceEntry.getValue()) {
                    try {
                        Reader reader = resource.openAsReader();
                        try {
                            JsonElement json = GsonHelper.fromJson(this.gson, reader, JsonElement.class);
                            // Resource condition stuff here
                            profiler.push(String.format("Borealib resource conditions: %s", this.directory));
                            if (json.isJsonObject() && ResourceConditionRegistry.test(json.getAsJsonObject())) {
                                list.add(json);
                            }
                            profiler.pop();
                        } catch (Throwable throwable) {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (Throwable throwable2) {
                                    throwable.addSuppressed(throwable2);
                                }
                            }
                            throw throwable;
                        }
                    } catch (Exception e) {
                        Borealib.LOGGER.error("Couldn't parse data file " + idToUse + " from " + rawId, e);
                    }
                }
            }
        }
        return map;
    }
}
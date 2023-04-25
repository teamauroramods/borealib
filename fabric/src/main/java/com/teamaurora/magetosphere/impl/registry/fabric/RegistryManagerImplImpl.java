package com.teamaurora.magetosphere.impl.registry.fabric;

import com.teamaurora.magetosphere.impl.registry.VanillaPlatformRegistry;
import com.teamaurora.magetosphere.api.registry.v1.PlatformRegistry;
import com.teamaurora.magetosphere.api.registry.v1.RegistryProperties;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryManagerImplImpl {

    private static final Map<ResourceLocation, PlatformRegistry<?>> REGISTRIES = new ConcurrentHashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> PlatformRegistry<T> getRegistry(ResourceLocation id) {
        return (PlatformRegistry<T>) REGISTRIES.computeIfAbsent(id, __ -> {
            Registry<T> vanillaRegistry = (Registry<T>) BuiltInRegistries.REGISTRY.get(id);
            if (vanillaRegistry != null)
                return new VanillaPlatformRegistry<>(vanillaRegistry);
            return null;
        });
    }

    public static <T> PlatformRegistry<T> createRegistry(ResourceLocation id, RegistryProperties<T> properties) {
        if (REGISTRIES.containsKey(id)) throw new IllegalArgumentException("Attempted to create duplicate registry:" + id);
        FabricRegistryBuilder<T, MappedRegistry<T>> builder = FabricRegistryBuilder.createSimple(ResourceKey.createRegistryKey(id));
        // Attribute-based properties
        if (properties.saveToDisk())
            builder.attribute(RegistryAttribute.PERSISTED);
        if (properties.syncToClients())
            builder.attribute(RegistryAttribute.SYNCED);
        Registry<T> vanillaRegistry = builder.buildAndRegister();
        PlatformRegistry<T> registry = new VanillaPlatformRegistry<>(vanillaRegistry);
        REGISTRIES.put(id, registry);
        if (!properties.getOnFill().isEmpty())
            properties.getOnFill().forEach(c -> c.accept(registry));
        if (!properties.getOnAdd().isEmpty())
            RegistryEntryAddedCallback.event(vanillaRegistry).register((id1, name, object) -> properties.getOnAdd().forEach(c -> c.onAdd(id1, name, object)));
        return registry;
    }
}

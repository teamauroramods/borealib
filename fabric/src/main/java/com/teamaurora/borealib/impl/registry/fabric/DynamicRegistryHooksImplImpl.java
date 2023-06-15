package com.teamaurora.borealib.impl.registry.fabric;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DynamicRegistryHooksImplImpl {

    private static final List<RegistryDataLoader.RegistryData<?>> DATA_REGISTRIES = new ArrayList<>();
    private static final Map<ResourceKey<Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> NETWORKABLE_DATA_REGISTRIES = new HashMap<>();
    private static final Set<ResourceLocation> KEYS = new HashSet<>();

    public static <T> ResourceKey<Registry<T>> create(ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        ResourceKey<Registry<T>> key = ResourceKey.createRegistryKey(id);
        DATA_REGISTRIES.add(new RegistryDataLoader.RegistryData<>(key, codec));
        KEYS.add(key.location());
        if (networkCodec != null)
            NETWORKABLE_DATA_REGISTRIES.put((ResourceKey) key, new RegistrySynchronization.NetworkedRegistryData<>(key, networkCodec));
        return key;
    }

    public static List<RegistryDataLoader.RegistryData<?>> getDataRegistries() {
        return DATA_REGISTRIES;
    }

    public static Map<ResourceKey<Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> getNetworkableDataRegistries() {
        return NETWORKABLE_DATA_REGISTRIES;
    }

    public static boolean shouldPrefix(ResourceLocation l) {
        return !l.getNamespace().equals("minecraft") && KEYS.contains(l);
    }
}

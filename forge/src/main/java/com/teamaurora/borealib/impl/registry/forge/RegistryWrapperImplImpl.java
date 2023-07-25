package com.teamaurora.borealib.impl.registry.forge;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.base.v1.util.forge.ForgeHelper;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.impl.registry.VanillaRegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
public class RegistryWrapperImplImpl {

    private static final Map<ResourceLocation, RegistryWrapper<?>> REGISTRIES = new ConcurrentHashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> RegistryWrapper<T> getRegistry(ResourceLocation id) {
        return (RegistryWrapper<T>) REGISTRIES.computeIfAbsent(id, __ -> {
            IForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(id);
            if (registry != null)
                return new ForgeRegistryWrapper<>(registry);
            return null;
        });
    }

    public static <T> RegistryWrapper.Provider<T> provider(ResourceKey<? extends Registry<T>> key, String owner) {
        return new ForgeRegistryWrapper.Provider<>(key, owner);
    }

    public static <T> ResourceKey<? extends Registry<T>> dynamicRegistry(ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        ResourceKey<Registry<T>> rkey = ResourceKey.createRegistryKey(id);
        ForgeHelper.getEventBus(id.getNamespace()).<DataPackRegistryEvent.NewRegistry>addListener(event -> event.dataPackRegistry(rkey, codec, networkCodec));
        return rkey;
    }
}

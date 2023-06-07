package com.teamaurora.borealib.impl.registry.forge;

import com.teamaurora.borealib.api.registry.v1.RegistryView;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
public class RegistryViewImplImpl {

    private static final Map<ResourceLocation, RegistryView<?>> REGISTRIES = new ConcurrentHashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> RegistryView<T> getRegistry(ResourceLocation id) {
        return (RegistryView<T>) REGISTRIES.computeIfAbsent(id, __ -> {
            IForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(id);
            if (registry != null)
                return new ForgeRegistryView<>(registry);
            return null;
        });
    }

    public static Set<Map.Entry<ResourceLocation, RegistryView<?>>> allRegistries() {
        return REGISTRIES.entrySet();
    }
}

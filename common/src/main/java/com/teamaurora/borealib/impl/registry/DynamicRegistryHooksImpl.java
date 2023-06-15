package com.teamaurora.borealib.impl.registry;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.RegistryAccess;

@ApiStatus.Internal
public class DynamicRegistryHooksImpl {

    /**
     * Registers a world-specific dynamic registry that loads its contents from Json using the provided {@link Codec}.
     * <p>Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     *
     * @param id           The name of the registry
     * @param codec        A codec to (de)serialize registry entries from JSON
     * @param networkCodec An optional codec to sync registry contents to clients
     * @param <T> The registry object type
     * @return The key to find registry contents on a server's {@link RegistryAccess}
     */
    @ExpectPlatform
    public static <T> ResourceKey<Registry<T>> create(ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        return Platform.expect();
    }

    /**
     * Registers a world-specific dynamic registry that loads its contents from Json using the provided {@link Codec}.
     * <p>Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     *
     * @param id           The name of the registry
     * @param codec        A codec to (de)serialize registry entries from JSON
     * @param <T> The registry object type
     * @return The key to find registry contents on a server's {@link RegistryAccess}
     */
    public static <T> ResourceKey<Registry<T>> create(ResourceLocation id, Codec<T> codec) {
        return create(id, codec, null);
    }
}
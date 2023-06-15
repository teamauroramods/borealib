package com.teamaurora.borealib.api.registry.v1;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.impl.registry.DynamicRegistryHooksImpl;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface DynamicRegistryHooks {

    static <T> ResourceKey<Registry<T>> create(ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        return DynamicRegistryHooksImpl.create(id, codec, networkCodec);
    }

    static <T> ResourceKey<Registry<T>> create(ResourceLocation id, Codec<T> codec) {
        return DynamicRegistryHooksImpl.create(id, codec);
    }
}

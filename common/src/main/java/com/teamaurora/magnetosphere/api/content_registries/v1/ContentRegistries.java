package com.teamaurora.magnetosphere.api.content_registries.v1;

import com.mojang.serialization.Codec;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import com.teamaurora.magnetosphere.impl.content_registries.ContentRegistriesImpl;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface ContentRegistries {

    @Nullable
    static <T, R> ContentRegistry<T, R> get(ResourceLocation registryId) {
        return ContentRegistriesImpl.get(registryId);
    }

    static <T, R> ContentRegistry<T, R> create(ResourceLocation registryId, RegistryView<T> parentRegistry, Codec<R> elementCodec) {
        return ContentRegistriesImpl.create(registryId, parentRegistry, elementCodec);
    }
}

package com.teamaurora.borealib.impl.registry.forge;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.base.v1.util.forge.ForgeHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.jetbrains.annotations.Nullable;

public class DynamicRegistryHooksImplImpl {

    public static <T> ResourceKey<Registry<T>> create(ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        ResourceKey<Registry<T>> rkey = ResourceKey.createRegistryKey(id);
        ForgeHelper.getEventBus(id.getNamespace()).<DataPackRegistryEvent.NewRegistry>addListener(event -> event.dataPackRegistry(rkey, codec, networkCodec));
        return rkey;
    }
}

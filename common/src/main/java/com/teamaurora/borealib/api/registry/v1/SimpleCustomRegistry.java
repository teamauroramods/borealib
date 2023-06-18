package com.teamaurora.borealib.api.registry.v1;

import com.mojang.serialization.Lifecycle;
import com.teamaurora.borealib.impl.registry.VanillaRegistryView;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class SimpleCustomRegistry<T> extends VanillaRegistryView<T> {

    private SimpleCustomRegistry(Registry<T> parent) {
        super(parent);
    }

    public static <T> SimpleCustomRegistry<T> create(ResourceLocation id) {
        return new SimpleCustomRegistry<>(new MappedRegistry<>(ResourceKey.createRegistryKey(id), Lifecycle.stable()));
    }

    public Registry<T> unwrap() {
        return this.parent;
    }
}
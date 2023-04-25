package dev.tesseract.api.registry.v1;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface RegistryReference<T> extends Supplier<T> {

    @Override
    T get();

    boolean isPresent();

    ResourceLocation getId();

    ResourceKey<T> getKey();

    Holder<T> getHolder();

    ResourceKey<? extends Registry<T>> getRegistryKey();
}

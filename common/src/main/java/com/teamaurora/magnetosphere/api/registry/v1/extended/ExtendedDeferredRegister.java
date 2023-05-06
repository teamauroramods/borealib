package com.teamaurora.magnetosphere.api.registry.v1.extended;

import com.teamaurora.magnetosphere.api.registry.v1.DeferredRegister;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryProperties;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Supplier;

public class ExtendedDeferredRegister<T> implements DeferredRegister<T> {

    private final DeferredRegister<T> parent;

    protected ExtendedDeferredRegister(DeferredRegister<T> parent) {
        this.parent = parent;
    }

    @Override
    public String id() {
        return this.parent.id();
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.parent.getRegistryKey();
    }

    @Override
    public <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object) {
        return this.parent.register(name, object);
    }

    @Override
    public Supplier<RegistryView<T>> makeRegistry(Supplier<RegistryProperties<T>> properties) {
        return this.parent.makeRegistry(properties);
    }

    @NotNull
    @Override
    public Iterator<RegistryReference<T>> iterator() {
        return this.parent.iterator();
    }
}

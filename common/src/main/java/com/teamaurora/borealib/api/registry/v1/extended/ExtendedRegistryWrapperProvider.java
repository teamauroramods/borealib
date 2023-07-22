package com.teamaurora.borealib.api.registry.v1.extended;

import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Supplier;

public class ExtendedRegistryWrapperProvider<T> implements RegistryWrapper.Provider<T> {

    private final RegistryWrapper.Provider<T> parent;

    protected ExtendedRegistryWrapperProvider(RegistryWrapper.Provider<T> parent) {
        this.parent = parent;
    }

    @Override
    public String owner() {
        return this.parent.owner();
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.parent.getRegistryKey();
    }

    @Override
    public <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object) {
        return this.parent.register(name, object);
    }

    @NotNull
    @Override
    public Iterator<RegistryReference<T>> iterator() {
        return this.parent.iterator();
    }
}

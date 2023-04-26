package com.teamaurora.magnetosphere.api.registry.v1;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class RegistryProperties<T> {

    private boolean saveToDisk = true;
    private boolean syncToClients = true;
    private final List<Consumer<PlatformRegistry<T>>> onFill = new ArrayList<>();
    private final List<OnAdd<T>> onAdd = new ArrayList<>();

    public RegistryProperties() {
    }

    /**
     * Disable saving of the registry to the disk
     */
    public RegistryProperties<T> disableSaving() {
        this.saveToDisk = false;
        return this;
    }

    /**
     * Disable syncing between servers and clients
     */
    public RegistryProperties<T> disableSync() {
        this.syncToClients = false;
        return this;
    }

    /**
     * Adds code to run when the registry is first created, on Fabric this is immediate and on Forge this happens when <code>NewRegistryEvent</code> is posted
     *
     * @param onFill Code to run
     */
    public RegistryProperties<T> onFill(Consumer<PlatformRegistry<T>> onFill) {
        this.onFill.add(onFill);
        return this;
    }

    /**
     * Adds code to run for each registry object added.
     *
     * @param onAdd The code to run
     */
    public RegistryProperties<T> onAdd(OnAdd<T> onAdd) {
        this.onAdd.add(onAdd);
        return this;
    }

    public boolean saveToDisk() {
        return this.saveToDisk;
    }

    public boolean syncToClients() {
        return this.syncToClients;
    }

    public List<Consumer<PlatformRegistry<T>>> getOnFill() {
        return this.onFill;
    }

    public List<OnAdd<T>> getOnAdd() {
        return this.onAdd;
    }

    @FunctionalInterface
    public interface OnAdd<T> {
        void onAdd(int id, ResourceLocation name, T object);
    }
}

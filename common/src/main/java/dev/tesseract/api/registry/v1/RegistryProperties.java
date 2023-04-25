package dev.tesseract.api.registry.v1;

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

    public RegistryProperties<T> disableSaving() {
        this.saveToDisk = false;
        return this;
    }

    public RegistryProperties<T> disableSync() {
        this.syncToClients = false;
        return this;
    }

    public RegistryProperties<T> onFill(Consumer<PlatformRegistry<T>> onFill) {
        this.onFill.add(onFill);
        return this;
    }

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

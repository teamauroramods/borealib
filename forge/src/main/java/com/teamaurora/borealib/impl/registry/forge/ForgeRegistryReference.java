package com.teamaurora.borealib.impl.registry.forge;

import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

@ApiStatus.Internal
@SuppressWarnings("unchecked")
public class ForgeRegistryReference<R extends T, T> implements RegistryReference<R> {
    private final ResourceLocation id;
    private final ResourceKey<R> key;
    private R value;
    private Holder<R> holder;

    public ForgeRegistryReference(ResourceLocation id, ResourceKey<? extends Registry<T>> registry) {
        this.id = id;
        this.key = (ResourceKey<R>) ResourceKey.create(registry, id);
    }

    @Override
    public R get() {
        return this.value;
    }
    @Override
    public boolean isPresent() {
        return this.value != null;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public ResourceKey<R> getKey() {
        return this.key;
    }

    @Override
    public Optional<Holder<R>> getHolder() {
        return Optional.ofNullable(this.holder);
    }

    void updateReference(RegisterEvent event) {
        IForgeRegistry<? extends T> forgeRegistry = event.getForgeRegistry();
        if (forgeRegistry != null) {
            if (forgeRegistry.containsKey(this.id)) {
                this.value = (R) forgeRegistry.getValue(this.id);
                this.holder = (Holder<R>) forgeRegistry.getHolder(this.id).orElse(null);
            } else {
                this.value = null;
                this.holder = null;
            }
            return;
        }
        Registry<T> vanillaRegistry = event.getVanillaRegistry();
        if (vanillaRegistry != null) {
            if (vanillaRegistry.containsKey(this.id)) {
                this.value = (R) vanillaRegistry.get(this.id);
                this.holder = (Holder<R>) vanillaRegistry.getHolder((ResourceKey<T>) this.key).orElse(null);
            } else {
                this.value = null;
                this.holder = null;
            }
        } else {
            this.value = null;
        }
    }
}

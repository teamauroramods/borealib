package com.teamaurora.borealib.impl.registry;

import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
@SuppressWarnings({"unchecked", "rawtypes"})
public class VanillaDeferredRegister<T> extends DeferredRegisterImpl<T> {

    private final Map<RegistryReferenceImpl<? extends T, T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    private final Registry<T> registry;

    public VanillaDeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        super(registryKey, modId);
        this.registry = (Registry<T>) Objects.requireNonNull(BuiltInRegistries.REGISTRY.get(this.registryKey.location()));
    }

    public VanillaDeferredRegister(Registry<T> registry, String modId) {
        super(registry.key(), modId);
        this.registry = registry;
    }

    @Override
    public <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object) {
        R registered = Registry.register(this.registry, name, object.get());
        return new RegistryReferenceImpl<>(name, this.registryKey, registered);
    }

    @NotNull
    @Override
    public Iterator<RegistryReference<T>> iterator() {
        return ((Set) this.entries.keySet()).iterator();
    }


    static class RegistryReferenceImpl<R extends T, T> implements RegistryReference<R> {
        private final ResourceLocation id;
        private final ResourceKey<R> key;
        private final R value;
        private Holder<R> holder;
        private final ResourceKey<? extends Registry<T>> registryKey;

        public RegistryReferenceImpl(ResourceLocation id, ResourceKey<? extends Registry<T>> registryKey, R value) {
            this.value = value;
            this.id = id;
            this.registryKey = registryKey;
            this.key = (ResourceKey<R>) ResourceKey.create(registryKey, id);
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

        @Override
        public void listen(Consumer<R> consumer) {
            consumer.accept(this.get());
        }
    }
}

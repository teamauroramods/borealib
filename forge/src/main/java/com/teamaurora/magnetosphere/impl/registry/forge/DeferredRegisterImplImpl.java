package com.teamaurora.magnetosphere.impl.registry.forge;

import com.mojang.serialization.Lifecycle;
import com.teamaurora.magnetosphere.api.base.v1.util.forge.ForgeHelper;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryProperties;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import com.teamaurora.magnetosphere.impl.registry.DeferredRegisterImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
@SuppressWarnings({"unchecked", "rawtypes"})
public class DeferredRegisterImplImpl<T> extends DeferredRegisterImpl<T> {

    private final Map<RegistryReferenceImpl<? extends T, T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    @Nullable
    private Supplier<RegistryBuilder<T>> registryFactory;
    @Nullable
    private Supplier<RegistryProperties<T>> propertiesSupplier;

    protected DeferredRegisterImplImpl(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        super(registryKey, modId);
        ForgeHelper.getEventBus(modId).register(new EventDispatcher<>(this));
    }

    public static <T> DeferredRegisterImpl<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return new DeferredRegisterImplImpl<>(registryKey, modId);
    }

    @Override
    public <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object) {
        RegistryReferenceImpl<R, T> ret = new RegistryReferenceImpl<>(name, this.registryKey);
        if (this.entries.putIfAbsent(ret, object) != null)
            throw new IllegalArgumentException("Duplicate registration " + name);
        return ret;
    }

    @Override
    public Supplier<RegistryView<T>> makeRegistry(Supplier<RegistryProperties<T>> properties) {
        if (RegistryManager.ACTIVE.getRegistry(this.registryKey) != null || this.registryFactory != null)
            throw new IllegalStateException("Cannot create a registry for a type that already exists");
        this.propertiesSupplier = properties;
        this.registryFactory = () -> {
            RegistryBuilder<T> builder = new RegistryBuilder<>();
            builder.setName(this.registryKey.location());
            if (!properties.get().saveToDisk())
                builder.disableSaving();
            if (!properties.get().syncToClients())
                builder.disableSync();
            if (!properties.get().getOnAdd().isEmpty())
                builder.onAdd((internal, manager, id, key, object, old) -> properties.get().getOnAdd().forEach(c -> c.onAdd(id, key.location(), object)));
            return builder;
        };
        return new RegistryHolder<>(this.registryKey);
    }

    @NotNull
    @Override
    public Iterator<RegistryReference<T>> iterator() {
        return ((Set) this.entries.keySet()).iterator();
    }

    private void addEntries(RegisterEvent event) {
        if (event.getRegistryKey().equals(this.registryKey)) {
            for (Map.Entry<RegistryReferenceImpl<? extends T, T>, Supplier<? extends T>> e : entries.entrySet()) {
                event.register(this.registryKey, e.getKey().getId(), () -> e.getValue().get());
                e.getKey().updateReference(event);
            }
        }
    }

    public static class EventDispatcher<T> {
        private final DeferredRegisterImplImpl<T> register;

        public EventDispatcher(final DeferredRegisterImplImpl<T> register) {
            this.register = register;
        }

        @SubscribeEvent
        public void handleEvent(NewRegistryEvent event) {
            if (register.registryFactory != null)
                event.create(register.registryFactory.get(), (registry) -> {
                    List<Consumer<RegistryView<T>>> onFill = register.propertiesSupplier.get().getOnFill();
                    if (!onFill.isEmpty()) {
                        RegistryView<T> view = new ForgeRegistryView<>(registry);
                        onFill.forEach(c -> c.accept(view));
                    }
                });
        }

        @SubscribeEvent
        public void handleEvent(RegisterEvent event) {
            register.addEntries(event);
        }
    }

    static class RegistryReferenceImpl<R extends T, T> implements RegistryReference<R> {
        private final ResourceLocation id;
        private final ResourceKey<R> key;
        private final List<Consumer<R>> onRegister = new ArrayList<>();
        private R value;
        private Holder<R> holder;

        public RegistryReferenceImpl(ResourceLocation id, ResourceKey<? extends Registry<T>> registry) {
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

        @Override
        public void listen(Consumer<R> consumer) {
            if (this.isPresent())
                consumer.accept(this.get());
            else
                this.onRegister.add(consumer);
        }

        void updateReference(RegisterEvent event) {
            IForgeRegistry<? extends T> forgeRegistry = event.getForgeRegistry();
            if (forgeRegistry != null) {
                if (forgeRegistry.containsKey(this.id)) {
                    this.value = (R) forgeRegistry.getValue(this.id);
                    this.holder = (Holder<R>) forgeRegistry.getHolder(this.id).orElse(null);
                    this.onRegister.forEach(c -> c.accept(this.value));
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
                    this.onRegister.forEach(c -> c.accept(this.value));
                } else {
                    this.value = null;
                    this.holder = null;
                }
            } else {
                this.value = null;
            }
        }
    }

    private static class RegistryHolder<V> implements Supplier<RegistryView<V>> {
        private final ResourceKey<? extends Registry<V>> registryKey;
        private RegistryView<V> registry = null;

        private RegistryHolder(ResourceKey<? extends Registry<V>> registryKey) {
            this.registryKey = registryKey;
        }

        @Override
        public RegistryView<V> get() {
            // Keep looking up the registry until it's not null
            if (this.registry == null)
                this.registry = new ForgeRegistryView<>(RegistryManager.ACTIVE.getRegistry(this.registryKey));
            return this.registry;
        }
    }
}

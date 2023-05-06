package com.teamaurora.magnetosphere.impl.registry.fabric;

import com.mojang.serialization.Lifecycle;
import com.teamaurora.magnetosphere.api.registry.v1.DeferredRegister;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryProperties;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryView;
import com.teamaurora.magnetosphere.impl.registry.DeferredRegisterImpl;
import com.teamaurora.magnetosphere.impl.registry.VanillaRegistryView;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
@SuppressWarnings({"unchecked", "rawtypes"})
public class DeferredRegisterImplImpl<T> extends DeferredRegisterImpl<T> {

    private static final Map<ResourceKey<? extends Registry<?>>, Map<String, List<DeferredRegisterImplImpl<?>>>> REGISTRIES = new LinkedHashMap<>();
    public static final List<ResourceKey<? extends Registry<?>>> REGISTRY_PRIORITY = List.of(
            Registries.SOUND_EVENT, Registries.FLUID, Registries.BLOCK, Registries.PARTICLE_TYPE,
            Registries.ENTITY_TYPE, Registries.ITEM,
            Registries.BLOCK_ENTITY_TYPE, Registries.PLACEMENT_MODIFIER_TYPE, Registries.STRUCTURE_TYPE,
            Registries.STRUCTURE_PIECE, Registries.FEATURE
    );

    // Add registries to prioritize first before custom or unlisted ones
    static {
        REGISTRY_PRIORITY.forEach(e -> REGISTRIES.put(e, new LinkedHashMap<>()));
    }

    private final Map<RegistryReferenceImpl<? extends T, T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    private boolean customMarker;

    private DeferredRegisterImplImpl(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        super(registryKey, modId);
    }

    // Ran by Magnetosphere to ensure a deterministic registry load order (Fabric loads mod initializers randomly)
    // Also allows for a commonPostInit where MGN-dependent registry objects are guaranteed to be loaded
    public static void init() {
        REGISTRIES.forEach((key, value) -> {
            List<String> sorted = value.keySet().stream().sorted().toList();
            sorted.forEach(s -> value.get(s).forEach(DeferredRegisterImplImpl::initEntries));
        });
    }

    public static <T> DeferredRegisterImpl<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        DeferredRegisterImplImpl<T> register = new DeferredRegisterImplImpl<>(registryKey, modId);
        Map<String, List<DeferredRegisterImplImpl<?>>> map = REGISTRIES.computeIfAbsent(registryKey, __ -> new LinkedHashMap<>());
        map.computeIfAbsent(modId, __ -> new ArrayList<>()).add(register);
        return register;
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
        if (BuiltInRegistries.REGISTRY.get(this.registryKey.location()) != null || this.customMarker)
            throw new IllegalStateException("Cannot create a registry for a type that already exists");
        this.customMarker = true;
        RegistryProperties<T> props = properties.get();
        FabricRegistryBuilder<T, MappedRegistry<T>> builder = FabricRegistryBuilder.createSimple((ResourceKey<Registry<T>>) this.registryKey);
        if (props.saveToDisk())
            builder.attribute(RegistryAttribute.PERSISTED);
        if (props.syncToClients())
            builder.attribute(RegistryAttribute.SYNCED);
        Registry<T> registry = builder.buildAndRegister();
        if (!props.getOnAdd().isEmpty())
            RegistryEntryAddedCallback.event(registry).register((rawId, id, object) -> props.getOnAdd().forEach(c -> c.onAdd(rawId, id, object)));
        RegistryView<T> registryView = new VanillaRegistryView<>(registry);
        if (!props.getOnFill().isEmpty())
            props.getOnFill().forEach(c -> c.accept(registryView));
        return () -> registryView;
    }

    @NotNull
    @Override
    public Iterator<RegistryReference<T>> iterator() {
        return ((Set) this.entries.keySet()).iterator();
    }

    private void initEntries() {
        this.entries.forEach((key, value) -> key.initialize((Supplier) value));
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

        void initialize(Supplier<? extends R> supplier) {
            Registry<T> registry = (Registry<T>) Objects.requireNonNull(BuiltInRegistries.REGISTRY.get(this.getId()), "Registry " + id + " doesn't exist");
            this.holder = ((WritableRegistry) registry).register(ResourceKey.create(registry.key(), id), supplier.get(), Lifecycle.stable());
            this.value = this.holder.value();
            this.onRegister.forEach(c -> c.accept(this.value));
        }
    }
}

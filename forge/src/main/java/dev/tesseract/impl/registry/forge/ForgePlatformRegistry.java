package dev.tesseract.impl.registry.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import dev.tesseract.api.base.v1.util.forge.TesseractForgeUtil;
import dev.tesseract.api.registry.v1.PlatformRegistry;
import dev.tesseract.api.registry.v1.RegistryProperties;
import dev.tesseract.api.registry.v1.RegistryReference;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.*;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class ForgePlatformRegistry<T> implements PlatformRegistry<T> {

    private final Supplier<IForgeRegistry<T>> registry;
    private final Map<String, DeferredRegister<T>> deferredRegisters = new ConcurrentHashMap<>();
    private final MutableBoolean initialized = new MutableBoolean();
    private final Codec<T> codec;
    private final ResourceKey<? extends Registry<T>> key;

    // constructor for existing registries
    ForgePlatformRegistry(IForgeRegistry<T> registry) {
        this.registry = () -> registry;
        this.codec = registry.getCodec();
        this.key = registry.getRegistryKey();
        initialized.setTrue();
    }

    // constructor for a custom registry
    ForgePlatformRegistry(ResourceKey<? extends Registry<T>> registryKey, RegistryProperties<T> properties) {
        this.registry = new RegistryHolder<>(registryKey);
        this.codec = ExtraCodecs.lazyInitializedCodec(() -> this.registry.get().getCodec());
        this.key = registryKey;
        TesseractForgeUtil.getEventBus(registryKey.location().getNamespace()).<NewRegistryEvent>addListener(e -> {
            RegistryBuilder<T> builder = new RegistryBuilder<T>().setName(registryKey.location());
            if (!properties.saveToDisk())
                builder.disableSaving();
            if (!properties.syncToClients())
                builder.disableSync();
            if (!properties.getOnAdd().isEmpty())
                builder.onAdd((internal, manager, id, name, object, old) -> properties.getOnAdd().forEach(c -> c.onAdd(id, name.location(), object)));
            e.create(builder, registry -> {
                initialized.setTrue();
                if (!properties.getOnFill().isEmpty())
                    properties.getOnFill().forEach(c -> c.accept(this));
            });
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends T> RegistryReference<R> register(ResourceLocation name, Supplier<? extends R> object) {
        DeferredRegister<T> registry1 = this.deferredRegisters.computeIfAbsent(name.getNamespace(), s -> {
            DeferredRegister<T> deferredRegister = DeferredRegister.create(this.key, s);
            deferredRegister.register(TesseractForgeUtil.getEventBus(s));
            return deferredRegister;
        });
        return (RegistryReference<R>) new ForgeRegistryReference<>(registry1.register(name.getPath(), object), this.key);
    }

    @Override
    public Codec<T> byNameCodec() {
        return this.codec;
    }

    @Override
    @Nullable
    public ResourceLocation getKey(T value) {
        return !initialized.getValue() ? null : this.registry.get().getKey(value);
    }

    @Override
    public int getId(@Nullable T value) {
        return !initialized.getValue() ? -1 : ((ForgeRegistry<T>) this.registry.get()).getID(value);
    }

    @Override
    @Nullable
    public T byId(int id) {
        return !initialized.getValue() ? null : ((ForgeRegistry<T>) this.registry.get()).getValue(id);
    }

    @Override
    public int size() {
        return !initialized.getValue() ? 0 : this.registry.get().getEntries().size();
    }

    @Override
    @Nullable
    public T get(@Nullable ResourceLocation name) {
        return !initialized.getValue() ? null : this.registry.get().getValue(name);
    }

    @Override
    public ResourceKey<? extends Registry<T>> key() {
        return this.key;
    }

    @Override
    public Set<ResourceLocation> keySet() {
        return !initialized.getValue() ? Collections.emptySet() : this.registry.get().getKeys();
    }

    @Override
    public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
        return !initialized.getValue() ? Collections.emptySet() : this.registry.get().getEntries();
    }

    @Override
    public boolean containsKey(ResourceLocation name) {
        return !initialized.getValue() ? false : this.registry.get().containsKey(name);
    }

    @Override
    public <T1> Stream<T1> keys(DynamicOps<T1> ops) {
        return !initialized.getValue() ? Stream.empty() : this.keySet().stream().map(rl -> ops.createString(rl.toString()));
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return !initialized.getValue() ? Collections.emptyIterator() : this.registry.get().iterator();
    }

    private static class RegistryHolder<V> implements Supplier<IForgeRegistry<V>> {
        private final ResourceKey<? extends Registry<V>> registryKey;
        private IForgeRegistry<V> registry = null;

        private RegistryHolder(ResourceKey<? extends Registry<V>> registryKey) {
            this.registryKey = registryKey;
        }

        public IForgeRegistry<V> get() {
            if (this.registry == null) {
                this.registry = RegistryManager.ACTIVE.getRegistry(this.registryKey);
            }
            return this.registry;
        }
    }
}

package com.teamaurora.borealib.core.mixin.fabric;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.extensions.MappedRegistryExtension;
import com.teamaurora.borealib.impl.registry.fabric.FabricRegistryTagManager;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;
import java.util.function.Supplier;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements MappedRegistryExtension<T> {

    @Unique
    private final Multimap<TagKey<T>, Supplier<T>> optionalTags = Multimaps.newSetMultimap(new IdentityHashMap<>(), HashSet::new);

    @Shadow
    public abstract HolderOwner<T> holderOwner();

    @Shadow
    public abstract ResourceKey<? extends Registry<T>> key();

    @Shadow
    private volatile Map<TagKey<T>, HolderSet.Named<T>> tags;

    @Shadow
    @Final
    private Map<T, Holder.Reference<T>> byValue;

    @Shadow
    protected abstract HolderSet.Named<T> createTag(TagKey<T> tagKey);

    @Override
    public void borealib$addOptionalTag(TagKey<T> tagKey, Set<? extends Supplier<T>> defaultValues) {
        this.optionalTags.putAll(tagKey, defaultValues);
    }

    @Override
    public Optional<Holder<T>> borealib$getHolder(T object) {
        return Optional.ofNullable(this.byValue.get(object));
    }

    @ModifyVariable(method = "bindTags", at = @At("STORE"))
    private Set<TagKey<T>> removeDefaults(Set<TagKey<T>> original) {
        if (this.getTags() != null) {
            Set<TagKey<T>> newSet = new HashSet<>(original);
            newSet.removeAll(this.optionalTags.keySet());
            return newSet;
        }
        return original;
    }

    @Inject(method = "bindTags", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addDefaultTags(Map<TagKey<T>, List<Holder<T>>> map, CallbackInfo ci, Map<Holder.Reference<T>, List<TagKey<T>>> map2, Map<TagKey<T>, HolderSet.Named<T>> map3) {
        // Copied from Forge's NamespacedWrapper
        if (this.getTags() != null) {
            Set<TagKey<T>> defaultedTags = Sets.difference(this.optionalTags.keySet(), map.keySet());
            defaultedTags.forEach((name) -> {
                List<Holder<T>> defaults = this.optionalTags.get(name).stream().map((valueSupplier) -> {
                    return this.borealib$getHolder(valueSupplier.get()).orElse(null);
                }).filter(Objects::nonNull).distinct().toList();
                defaults.forEach((holder) -> {
                    this.addTagToHolder(map2, name, holder);
                });
                map3.computeIfAbsent(name, this::createTag).bind(defaults);
            });
        }
    }

    @Inject(method = "bindTags", at = @At("TAIL"))
    private void bindTagManager(Map<TagKey<T>, List<Holder<T>>> map, CallbackInfo ci) {
        FabricRegistryTagManager<T> tagManager = this.getTags();
        if (tagManager != null)
            tagManager.bind(this.tags);
    }

    @Unique
    private FabricRegistryTagManager<T> getTags() {
        RegistryWrapper<T> wrapper = RegistryWrapper.get(this.key());
        return wrapper != null ? (FabricRegistryTagManager<T>) wrapper.tags() : null;
    }

    @Unique
    private void addTagToHolder(Map<Holder.Reference<T>, List<TagKey<T>>> holderToTag, TagKey<T> name, Holder<T> holder) {
        if (!holder.canSerializeIn(this.holderOwner())) {
            throw new IllegalStateException("Can't create named set " + name + " containing value " + holder + " from outside registry " + this);
        } else if (!(holder instanceof Holder.Reference)) {
            throw new IllegalStateException("Found direct holder " + holder + " value in tag " + name);
        } else {
            holderToTag.get(holder).add(name);
        }
    }
}

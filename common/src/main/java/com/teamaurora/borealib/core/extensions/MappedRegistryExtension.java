package com.teamaurora.borealib.core.extensions;

import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@ApiStatus.Internal
public interface MappedRegistryExtension<T> {

    void borealib$addOptionalTag(TagKey<T> tagKey, Set<? extends Supplier<T>> defaultValues);

    Optional<Holder<T>> borealib$getHolder(T object);
}

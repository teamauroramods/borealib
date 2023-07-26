package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.api.registry.v1.RegistryTagManager;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.Stream;

@Mixin(Holder.class)
public interface HolderMixin<T> extends RegistryTagManager.ReverseTagData<T> {

    @Shadow
    Stream<TagKey<T>> tags();

    @Shadow
    boolean is(TagKey<T> tagKey);

    @Override
    default Stream<TagKey<T>> getTagKeys() {
        return this.tags();
    }

    @Override
    default boolean containsTag(TagKey<T> tagKey) {
        return this.is(tagKey);
    }
}

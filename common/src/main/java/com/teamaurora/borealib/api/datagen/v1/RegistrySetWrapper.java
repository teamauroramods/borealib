package com.teamaurora.borealib.api.datagen.v1;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

/**
 * Wraps platform-specific methods for dynamic registry datagen.
 *
 * @author ebo2022
 * @since 1.0
 */
@ApiStatus.NonExtendable
public interface RegistrySetWrapper {

     /**
      * Adds a bootstrap function to generate dynamic registry elements.
      *
      * @param registry  The registry to add a bootstrap function for
      * @param bootstrap The bootstrap handler
      * @param <T>       The registry object type
      */
     <T> RegistrySetWrapper add(ResourceKey<? extends Registry<T>> registry, RegistrySetBuilder.RegistryBootstrap<T> bootstrap);
}
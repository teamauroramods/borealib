package com.teamaurora.borealib.impl.registry.fabric;

import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.impl.registry.DeferredRegisterImpl;
import com.teamaurora.borealib.impl.registry.VanillaDeferredRegister;
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
public class DeferredRegisterImplImpl {

    public static <T> DeferredRegisterImpl<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return new VanillaDeferredRegister<>(registryKey, modId);
    }
}

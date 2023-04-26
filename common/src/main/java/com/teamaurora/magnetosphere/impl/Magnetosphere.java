package com.teamaurora.magnetosphere.impl;

import com.teamaurora.magnetosphere.api.base.v1.modloading.ModLoaderService;
import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

@ApiStatus.Internal
public class Magnetosphere {
    public static final String MOD_ID = "magnetosphere";
    public static ModLoaderService findMod(String id) {
        return ServiceLoader.load(ModLoaderService.class)
                .stream()
                .filter(p -> p.get().id().equals(id))
                .findFirst()
                .map(ServiceLoader.Provider::get)
                .orElseThrow(() -> new IllegalStateException("Couldn't find mod with the id" + id));
    }
}

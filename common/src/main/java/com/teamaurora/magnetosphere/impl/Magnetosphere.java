package com.teamaurora.magnetosphere.impl;

import com.teamaurora.magnetosphere.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.magnetosphere.api.base.v1.util.tabs.ModifyCreativeTabEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

@ApiStatus.Internal
public class Magnetosphere implements ModLoaderService {
    public static final String MOD_ID = "magnetosphere";
    public static ModLoaderService findMod(String id) {
        return ServiceLoader.load(ModLoaderService.class)
                .stream()
                .filter(p -> p.get().id().equals(id))
                .findFirst()
                .map(ServiceLoader.Provider::get)
                .orElseThrow(() -> new IllegalStateException("Couldn't find mod service with the id" + id));
    }

    @Override
    public String id() {
        return MOD_ID;
    }

    @Override
    public void onCommonInit() {
    }

    @Override
    public void onCommonPostInit(ParallelDispatcher dispatcher) {
    }
}

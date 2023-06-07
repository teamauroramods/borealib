package com.teamaurora.borealib.impl.config.fabric;

import com.teamaurora.borealib.api.config.v1.ConfigBuilder;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

@ApiStatus.Internal
public class ConfigRegistryImplImpl {

    public static <T> T register(String modId, ModConfig.Type type, String fileName, Function<ConfigBuilder, T> consumer) {
        Pair<T, FabricConfigSpec> pair = new ConfigBuilderImpl().configure(consumer);
        ConfigTracker.INSTANCE.trackConfig(new ModConfigImpl(type, pair.getRight(), FabricLoader.getInstance().getModContainer(modId).orElseThrow(() -> new
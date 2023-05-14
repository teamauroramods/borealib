package com.teamaurora.magnetosphere.impl.config.fabric;

import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.api.config.v1.ConfigBuilder;
import com.teamaurora.magnetosphere.api.config.v1.ModConfig;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

@ApiStatus.Internal
public class ConfigRegistryImplImpl {

    public static <T> T register(String modId, ModConfig.Type type, String fileName, Function<ConfigBuilder, T> consumer) {
        Pair<T, FabricConfigSpec> pair = new ConfigBuilderImpl().configure(consumer);
        ConfigTracker.INSTANCE.trackConfig(new ModConfigImpl(type, pair.getRight(), FabricLoader.getInstance().getModContainer(modId).orElseThrow(() -> new IllegalStateException("Unknown mod: " + modId)), fileName));
        return pair.getLeft();
    }

    public static Optional<ModConfig> get(String modId, ModConfig.Type type) {
        return ConfigTracker.INSTANCE.getConfig(modId, type);
    }
}

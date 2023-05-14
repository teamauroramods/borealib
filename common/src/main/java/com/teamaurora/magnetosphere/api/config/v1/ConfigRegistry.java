package com.teamaurora.magnetosphere.api.config.v1;

import com.teamaurora.magnetosphere.impl.config.ConfigRegistryImpl;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public interface ConfigRegistry {

    static <T> T register(String modId, ModConfig.Type type, Function<ConfigBuilder, T> consumer) {
        return register(modId, type, String.format("%s-%s.toml", modId, type.name().toLowerCase(Locale.ROOT)), consumer);
    }

    static <T> T register(String modId, ModConfig.Type type, String fileName, Function<ConfigBuilder, T> consumer) {
        return ConfigRegistryImpl.register(modId, type, fileName, consumer);
    }
    static Optional<ModConfig> get(String modId, ModConfig.Type type) {
        return ConfigRegistryImpl.get(modId, type);
    }
}
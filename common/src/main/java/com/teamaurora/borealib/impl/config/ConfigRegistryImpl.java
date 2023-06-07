package com.teamaurora.borealib.impl.config;

import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.config.v1.ConfigBuilder;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.function.Function;

@ApiStatus.Internal
public class ConfigRegistryImpl {

    @ExpectPlatform
    public static <T> T register(String modId, ModConfig.Type type, String fileName, Function<ConfigBuilder, T> consumer) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static Optional<ModConfig> get(String modId, ModConfig.Type type) {
        return Platform.expect();
    }
}

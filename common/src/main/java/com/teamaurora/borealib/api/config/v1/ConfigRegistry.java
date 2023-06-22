package com.teamaurora.borealib.api.config.v1;

import com.teamaurora.borealib.impl.config.ConfigRegistryImpl;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * Controls and allows for mods to locate configs provided by Borealib-dependent mods.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface ConfigRegistry {

    /**
     * Registers a new config with a formatted file name.
     *
     * @param modId    The name of the mod to make the config for
     * @param type     The type of config; only one of each can exist for a given mod
     * @param consumer A function to create the holding config object
     * @param <T>      The object to hold assigned config values
     * @return The config object constructed using the config builder
     */
    static <T> T register(String modId, ModConfig.Type type, Function<ConfigBuilder, T> consumer) {
        return register(modId, type, String.format("%s-%s.toml", modId, type.name().toLowerCase(Locale.ROOT)), consumer);
    }

    /**
     * Registers a new config with a custom file name.
     *
     * @param modId    The name of the mod to make the config for
     * @param type     The type of config; only one of each can exist for a given mod
     * @param fileName The name to use for the config file
     * @param consumer A function to create the holding config object
     * @param <T>      The object to hold assigned config values
     * @return The config object constructed using the config builder
     */
    static <T> T register(String modId, ModConfig.Type type, String fileName, Function<ConfigBuilder, T> consumer) {
        return ConfigRegistryImpl.register(modId, type, fileName, consumer);
    }

    /**
     * Gets the specified config provided by a Borealib-dependent mod.
     *
     * @param modId The id of the mod providing the config
     * @param type  The config type to look for
     * @return The config if it exists, otherwise {@link Optional#empty()}
     */
    static Optional<ModConfig> get(String modId, ModConfig.Type type) {
        return ConfigRegistryImpl.get(modId, type);
    }
}

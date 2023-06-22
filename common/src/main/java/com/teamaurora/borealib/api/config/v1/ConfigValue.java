package com.teamaurora.borealib.api.config.v1;

import java.util.List;
import java.util.function.Supplier;

/**
 * Represents a configurable object that can be changed in a config file. Based on Forge's config system.
 *
 * @param <T> The type of config object
 * @author ebo2022
 * @since 1.0
 */
public interface ConfigValue<T> extends Supplier<T> {

    List<String> getPath();

    /**
     * @return The config value if it's loaded, otherwise throws an exception
     */
    @Override
    T get();

    ConfigBuilder next();

    void save();

    void set(T value);

    void clearCache();
}

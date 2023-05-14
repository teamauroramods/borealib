package com.teamaurora.magnetosphere.api.config.v1;

import java.util.List;
import java.util.function.Supplier;

public interface ConfigValue<T> extends Supplier<T> {

    List<String> getPath();

    @Override
    T get();

    ConfigBuilder next();

    void save();

    void set(T value);

    void clearCache();
}

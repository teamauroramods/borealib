package com.teamaurora.magnetosphere.impl.config.forge;

import com.teamaurora.magnetosphere.api.config.v1.ConfigBuilder;
import com.teamaurora.magnetosphere.api.config.v1.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class ConfigValueImpl<T> implements ConfigValue<T> {

    private final ForgeConfigSpec.ConfigValue<T> configValue;

    ConfigValueImpl(ForgeConfigSpec.ConfigValue<T> configValue) {
        this.configValue = configValue;
    }

    @Override
    public List<String> getPath() {
        return this.configValue.getPath();
    }

    @Override
    public T get() {
        return this.configValue.get();
    }

    @Override
    public ConfigBuilder next() {
        return new ConfigBuilderImpl(this.configValue.next());
    }

    @Override
    public void save() {
        this.configValue.save();
    }

    @Override
    public void set(T value) {
        this.configValue.set(value);
    }

    @Override
    public void clearCache() {
        this.configValue.clearCache();
    }
}
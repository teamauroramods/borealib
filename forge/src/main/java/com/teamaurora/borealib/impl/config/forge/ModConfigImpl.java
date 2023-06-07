package com.teamaurora.borealib.impl.config.forge;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;

@ApiStatus.Internal
public class ModConfigImpl implements com.teamaurora.borealib.api.config.v1.ModConfig {

    private final ModConfig config;

    public ModConfigImpl(ModConfig config) {
        this.config = config;
    }

    @Override
    public com.teamaurora.borealib.api.config.v1.ModConfig.Type getType() {
        return ConfigRegistryImplImpl.convert(this.config.getType());
    }

    @Override
    public String getFileName() {
        return this.config.getFileName();
    }

    @Override
    public UnmodifiableConfig getSpec() {
        return this.config.getSpec();
    }

    @Override
    public String getModId() {
        return this.config.getModId();
    }

    @Override
    public CommentedConfig getConfigData() {
        return this.config.getConfigData();
    }

    @Override
    public void save() {
        this.config.save();
    }

    @Override
    public Path getFullPath() {
        return this.config.getFullPath();
    }
}
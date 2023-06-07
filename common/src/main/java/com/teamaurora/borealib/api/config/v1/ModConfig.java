package com.teamaurora.borealib.api.config.v1;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public interface ModConfig {

    Type getType();

    String getFileName();

    UnmodifiableConfig getSpec();

    String getModId();

    CommentedConfig getConfigData();

    void save();

    @Nullable
    Path getFullPath();

    enum Type {
        CLIENT,
        COMMON,
        SERVER
    }
}
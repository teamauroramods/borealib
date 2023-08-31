package com.teamaurora.borealib.api.config.v1;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Represents the data for a config file. Based on Forge's config system.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface ModConfig {

    Codec<ModConfig> CODEC = ResourceLocation.CODEC.comapFlatMap(location -> {
        Optional<ModConfig> config = ConfigRegistry.get(location.getNamespace(), Type.CODEC.byName(location.getPath()));
        if (config.isEmpty() || config.get().getConfigData() == null)
            return DataResult.error(() -> "Unknown config: " + location.toString());
        return DataResult.success(config.get());
    }, config -> {
       return new ResourceLocation(config.getModId(), config.getType().name);
    });

    Type getType();

    String getFileName();

    UnmodifiableConfig getSpec();

    String getModId();

    CommentedConfig getConfigData();

    void save();

    @Nullable
    Path getFullPath();

    enum Type implements StringRepresentable {

        CLIENT("client"),
        COMMON("common"),
        SERVER("server");

        private final String name;
        public static final StringRepresentable.EnumCodec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        Type(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
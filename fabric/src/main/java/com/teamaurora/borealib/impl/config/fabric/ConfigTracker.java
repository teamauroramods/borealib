package com.teamaurora.borealib.impl.config.fabric;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import com.teamaurora.borealib.api.event.config.v1.ConfigEvents;
import com.teamaurora.borealib.core.network.fabric.ClientboundSyncConfigDataPacket;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApiStatus.Internal
public class ConfigTracker {

    public static final ConfigTracker INSTANCE = new ConfigTracker();
    private static final Logger LOGGER = LogManager.getLogger();
    private final ConcurrentHashMap<String, ModConfigImpl> fileMap;
    private final EnumMap<ModConfig.Type, Set<ModConfigImpl>> configSets;
    private final ConcurrentHashMap<String, Map<ModConfig.Type, ModConfigImpl>> configsByMod;

    private ConfigTracker() {
        this.fileMap = new ConcurrentHashMap<>();
        this.configSets = new EnumMap<>(ModConfig.Type.class);
        this.configsByMod = new ConcurrentHashMap<>();
        for (ModConfig.Type type : ModConfig.Type.values())
            this.configSets.put(type, Collections.synchronizedSet(new LinkedHashSet<>()));
    }

    void trackConfig(ModConfigImpl config) {
        if (this.fileMap.containsKey(config.getFileName())) {
            LOGGER.error("Detected config file conflict {} between {} and {}", config.getFileName(), this.fileMap.get(config.getFileName()).getModId(), config.getModId());
            throw new RuntimeException("Config conflict detected!");
        }
        this.fileMap.put(config.getFileName(), config);
        this.configSets.get(config.getType()).add(config);
        this.configsByMod.computeIfAbsent(config.getModId(), (k) -> new EnumMap<>(ModConfig.Type.class)).put(config.getType(), config);
        LOGGER.debug("Config file {} for {} tracking", config.getFileName(), config.getModId());
    }

    public void loadConfigs(ModConfig.Type type, Path configBasePath) {
        LOGGER.debug("Loading configs type {}", type);
        this.configSets.get(type).forEach(config -> openConfig(config, configBasePath));
    }

    public void unloadConfigs(ModConfig.Type type, Path configBasePath) {
        LOGGER.debug("Unloading configs type {}", type);
        this.configSets.get(type).forEach(config -> closeConfig(config, configBasePath));
    }

    public List<Pair<String, ClientboundSyncConfigDataPacket>> syncConfigs(boolean isLocal) { // Only sync configs for players joining and if the config actually exists
        return isLocal ? Collections.emptyList() : this.configSets.get(ModConfig.Type.SERVER).stream().filter(mc -> mc.getFullPath() != null).map(mc -> {
            try {
                return Pair.of("Config " + mc.getFileName(), new ClientboundSyncConfigDataPacket(mc.getFileName(), Files.readAllBytes(mc.getFullPath())));
            } catch (Exception e) {
                LOGGER.error("Failed to sync {} config for {}", mc.getType(), mc.getModId(), e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void openConfig(ModConfigImpl config, Path configBasePath) {
        CommentedFileConfig configData = config.getHandler().reader(configBasePath).apply(config);
        config.setConfigData(configData);
        ConfigEvents.LOADING.invoker().accept(config);
        config.save();
    }

    private void closeConfig(ModConfigImpl config, Path configBasePath) {
        if (config.getConfigData() != null) {
            config.save();
            config.getHandler().unload(configBasePath, config);
            config.setConfigData(null);
        }
    }

    public void receiveSyncedConfig(ClientboundSyncConfigDataPacket pkt) {
        if (!Minecraft.getInstance().isLocalServer() && this.fileMap.containsKey(pkt.getFileName())) {
            ModConfigImpl config = this.fileMap.get(pkt.getFileName());
            config.setConfigData(TomlFormat.instance().createParser().parse(new ByteArrayInputStream(pkt.getFileData())));
            ConfigEvents.RELOADING.invoker().accept(config);
        }
    }

    public void loadDefaultServerConfigs() {
        this.configSets.get(ModConfig.Type.SERVER).forEach(config -> {
            CommentedConfig commentedConfig = CommentedConfig.inMemory();
            config.getSpec().correct(commentedConfig);
            config.setConfigData(commentedConfig);
            ConfigEvents.LOADING.invoker().accept(config);
        });
    }

    @Nullable
    public String getConfigFileName(String modId, ModConfig.Type type) {
        return Optional.ofNullable(this.configsByMod.getOrDefault(modId, Collections.emptyMap()).getOrDefault(type, null)).flatMap(config -> Optional.ofNullable(config.getFullPath())).map(Object::toString).orElse(null);
    }

    public Optional<ModConfigImpl> getConfig(String modId, ModConfig.Type type) {
        return Optional.ofNullable(this.configsByMod.getOrDefault(modId, Collections.emptyMap()).getOrDefault(type, null));
    }
}
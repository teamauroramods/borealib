package com.teamaurora.magnetosphere.impl.base.platform.fabric;

import com.teamaurora.magnetosphere.api.base.v1.platform.ModContainer;
import com.teamaurora.magnetosphere.api.base.v1.platform.Platform;
import com.teamaurora.magnetosphere.impl.fabric.MagnetosphereFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlatformImplImpl {

    private static final Supplier<Supplier<BlockableEventLoop<?>>> CLIENT_EXECUTOR = () -> Minecraft::getInstance;
    private static final Map<String, ModContainer> MOD_CONTAINERS = FabricLoader.getInstance().getAllMods().stream().map(ModContainerImpl::new).collect(Collectors.toConcurrentMap(ModContainerImpl::getId, Function.identity()));


    public static boolean isForge() {
        return false;
    }

    public static boolean isFabric() {
        return true;
    }

    public static boolean isDevelopment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static boolean isProduction() {
        return !FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    public static boolean isServer() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }

    public static boolean isOptifrickLoaded() {
        return isModLoaded("optifabric");
    }

    public static BlockableEventLoop<?> getGameExecutor() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? CLIENT_EXECUTOR.get().get() : Platform.getRunningServer().orElseThrow(IllegalStateException::new);
    }

    public static Path getGameDir() {
        return FabricLoader.getInstance().getGameDir().toAbsolutePath().normalize();
    }

    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir().toAbsolutePath().normalize();
    }

    public static Path getModsDir() {
        return getGameDir().resolve("mods");
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static Stream<ModContainer> getMods() {
        return MOD_CONTAINERS.values().stream();
    }

    public static Optional<ModContainer> getMod(String modId) {
        return Optional.ofNullable(MOD_CONTAINERS.get(modId));
    }

    public static Optional<MinecraftServer> getRunningServer() {
        return Optional.ofNullable(MagnetosphereFabric.getServer());
    }
}

package dev.tesseract.api.platform.fabric;

import dev.tesseract.api.platform.ModContainer;
import dev.tesseract.api.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.thread.BlockableEventLoop;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PlatformImpl {

    private static final Supplier<Supplier<BlockableEventLoop<?>>> CLIENT_EXECUTOR = () -> Minecraft::getInstance;

    public static boolean isDevelopment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static boolean isProduction() {
        return !FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static Platform.Env getEnv() {
        return Platform.Env.fromPlatform(FabricLoader.getInstance().getEnvironmentType());
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
        return FabricLoader.getInstance().getAllMods().stream().map(ModContainerImpl::new);
    }

    public static Optional<ModContainer> getMod(String modId) {
        return FabricLoader.getInstance().getModContainer(modId).map(ModContainerImpl::new);
    }
}

package dev.tesseract.impl.base.platform;

import dev.tesseract.api.base.v1.platform.ModContainer;
import dev.tesseract.api.base.v1.platform.Platform;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class PlatformImpl {

    public static boolean isForge() {
        return Platform.expect();
    }

    public static boolean isFabric() {
        return PlatformImpl.isFabric();
    }

    public static boolean isQuilt() {
        return PlatformImpl.isQuilt();
    }

    public static void ifForge(Runnable runnable) {
        if (isForge()) runnable.run();
    }

    public static void ifFabric(Runnable runnable) {
        if (isFabric()) runnable.run();
    }

    public static void ifQuilt(Runnable runnable) {
        if (isQuilt()) runnable.run();
    }

    public static boolean isDevelopment() {
        return PlatformImpl.isDevelopment();
    }

    public static boolean isProduction() {
        return PlatformImpl.isProduction();
    }

    public static boolean isClient() {
        return PlatformImpl.isClient();
    }

    public static boolean isServer() {
        return PlatformImpl.isServer();
    }

    public static boolean isOptifrickLoaded() {
        return PlatformImpl.isOptifrickLoaded();
    }

    public static BlockableEventLoop<?> getGameExecutor() {
        return PlatformImpl.getGameExecutor();
    }

    public static String getGameVersion() {
        return PlatformImpl.getGameVersion();
    }

    public static Path getGameDir() {
        return PlatformImpl.getGameDir();
    }

    public static Path getConfigDir() {
        return PlatformImpl.getConfigDir();
    }

    public static Path getModsDir() {
        return PlatformImpl.getModsDir();
    }

    public static boolean isModLoaded(String modId) {
        return PlatformImpl.isModLoaded(modId);
    }

    public static boolean areAllLoaded(String... modIds) {
        if (modIds == null) return true;
        for (String modId : modIds) {
            if (!isModLoaded(modId)) {
                return false;
            }
        }
        return true;
    }

    public static boolean areAnyLoaded(String... modIds) {
        if (modIds == null) return true;
        for (String modId : modIds) {
            if (isModLoaded(modId)) {
                return true;
            }
        }
        return false;
    }

    public static Stream<ModContainer> getMods() {
        return PlatformImpl.getMods();
    }

    public static Optional<ModContainer> getMod(String modId) {
        return PlatformImpl.getMod(modId);
    }

    public static Optional<MinecraftServer> getRunningServer() {
        return PlatformImpl.getRunningServer();
    }

    public static Optional<RegistryAccess> getRegistryAccess() {
        return PlatformImpl.getRegistryAccess();
    }

    public static Optional<RecipeManager> getRecipeManager() {
        return PlatformImpl.getRecipeManager();
    }
}

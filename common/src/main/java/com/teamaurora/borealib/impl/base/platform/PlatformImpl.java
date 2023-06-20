package com.teamaurora.borealib.impl.base.platform;

import com.teamaurora.borealib.api.base.v1.platform.ModContainer;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.base.v1.platform.Environment;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.SharedConstants;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@ApiStatus.Internal
public final class PlatformImpl {

    private static boolean QUILT;

    static {
        try {
            Class.forName("org.quiltmc.loader.api.QuiltLoader");
            QUILT = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    @ExpectPlatform
    public static boolean isForge() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean isFabric() {
        return Platform.expect();
    }

    public static boolean isQuilt() {
        return isFabric() && QUILT;
    }

    @ExpectPlatform
    public static boolean isDevelopment() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean isProduction() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static Environment getEnvironment() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean isOptifrickLoaded() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static BlockableEventLoop<?> getGameExecutor() {
        return Platform.expect();
    }

    public static String getGameVersion() {
        return SharedConstants.getCurrentVersion().getId();
    }

    @ExpectPlatform
    public static Path getGameDir() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static Path getConfigDir() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static Path getModsDir() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        return Platform.expect();
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

    @ExpectPlatform
    public static Stream<ModContainer> getMods() {
        return Platform.expect();
    }

    @ExpectPlatform
    public static Optional<ModContainer> getMod(String modId) {
        return Platform.expect();
    }

    @ExpectPlatform
    public static Optional<MinecraftServer> getRunningServer() {
        return Platform.expect();
    }
}

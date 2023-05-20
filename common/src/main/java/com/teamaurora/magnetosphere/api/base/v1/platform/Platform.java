package com.teamaurora.magnetosphere.api.base.v1.platform;

import com.teamaurora.magnetosphere.impl.base.platform.PlatformImpl;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.item.crafting.RecipeManager;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Common abstractions for modloader-specific code.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface Platform {

    /**
     * Used in the context of <code>@ExpectPlatform</code>. Use this as a placeholder for the return value
     */
    static <T> T expect() {
        throw new AssertionError();
    }

    /**
     * @return Whether the current platform is Forge
     */
    static boolean isForge() {
        return PlatformImpl.isForge();
    }

    /**
     * @return Whether the current platform is Fabric
     */
    static boolean isFabric() {
        return PlatformImpl.isFabric();
    }

    /**
     * @return Whether the current platform is Quilt
     */
    static boolean isQuilt() {
        return PlatformImpl.isQuilt();
    }

    /**
     * Runs the specified code if the current platform is Forge.
     *
     * @param runnable The code to run
     */
    static void ifForge(Runnable runnable) {
        if (isForge()) runnable.run();
    }

    /**
     * Runs the specified code if the current platform is Fabric.
     *
     * @param runnable The code to run
     */
    static void ifFabric(Runnable runnable) {
        if (isFabric()) runnable.run();
    }

    /**
     * Runs the specified code if the current platform is Quilt.
     *
     * @param runnable The code to run
     */
    static void ifQuilt(Runnable runnable) {
        if (isQuilt()) runnable.run();
    }

    /**
     * @return Whether the mod is running in a development environment
     */
    static boolean isDevelopment() {
        return PlatformImpl.isDevelopment();
    }

    /**
     * @return Whether the mod is running in a production environment
     */
    static boolean isProduction() {
        return PlatformImpl.isProduction();
    }

    /**
     * @return Whether the environment the game is running on is a client
     */
    static boolean isClient() {
        return PlatformImpl.isClient();
    }

    /**
     * @return Whether the environment the game is running on is a client
     */
    static boolean isServer() {
        return PlatformImpl.isServer();
    }

    /**
     * @return Whether Optifrick is breaking the game
     */
    static boolean isOptifrickLoaded() {
        return PlatformImpl.isOptifrickLoaded();
    }

    /**
     * @return The main game executor. This is the Minecraft client or server instance
     */
    static BlockableEventLoop<?> getGameExecutor() {
        return PlatformImpl.getGameExecutor();
    }

    /**
     * @return The current Minecraft version
     */
    static String getGameVersion() {
        return PlatformImpl.getGameVersion();
    }

    /**
     * @return The root directory for the current Minecraft instance
     */
    static Path getGameDir() {
        return PlatformImpl.getGameDir();
    }

    /**
     * @return The config directory for the current Minecraft instance
     */
    static Path getConfigDir() {
        return PlatformImpl.getConfigDir();
    }

    /**
     * @return The mod folder for the current Minecraft instance
     */
    static Path getModsDir() {
        return PlatformImpl.getModsDir();
    }

    /**
     * Checks to see if the specified mod is loaded.
     *
     * @param modId The mod to check for
     * @return Whether the given mod is loaded
     */
    static boolean isModLoaded(String modId) {
        return PlatformImpl.isModLoaded(modId);
    }

    /**
     * Checks to see if all the specified mods are loaded.
     *
     * @param modIds The mods to check for
     * @return Whether all the given mods are loaded
     */
    static boolean allModsLoaded(String... modIds) {
        if (modIds == null) return true;
        for (String modId : modIds) {
            if (!isModLoaded(modId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to see if any of the specified mods are loaded.
     *
     * @param modIds The mods to check for
     * @return Whether all the given mods are loaded
     */
    static boolean anyModsLoaded(String... modIds) {
        if (modIds == null) return true;
        for (String modId : modIds) {
            if (isModLoaded(modId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return A stream of all loaded mods
     */
    static Stream<ModContainer> getMods() {
        return PlatformImpl.getMods();
    }

    /**
     * Gets a {@link ModContainer} using the specified id.
     *
     * @param modId The id of the mod to find
     * @return A mod container if the specified mod is present, otherwise {@link Optional#empty()}
     */
    static Optional<ModContainer> getMod(String modId) {
        return PlatformImpl.getMod(modId);
    }

    /**
     * @return The currently running Minecraft server. This will not be present in a remote client level
     */
    static Optional<MinecraftServer> getRunningServer() {
        return PlatformImpl.getRunningServer();
    }

    static Optional<RegistryAccess> getRegistryAccess() {
        return getRunningServer().map(MinecraftServer::registryAccess);
    }
}

package dev.tesseract.api.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import dev.tesseract.core.Tesseract;
import net.fabricmc.api.EnvType;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.item.crafting.RecipeManager;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class Platform {

    private static final boolean FORGE = ArchitecturyTarget.getCurrentTarget().equals(PlatformOnly.FORGE);
    private static final boolean FABRIC = ArchitecturyTarget.getCurrentTarget().equals(PlatformOnly.FABRIC);
    private static final Supplier<RegistryAccess> CLIENT_REGISTRY_ACCESS = () -> {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        return listener != null ? listener.registryAccess() : null;
    };
    private static final Supplier<RecipeManager> CLIENT_RECIPE_MANAGER = () -> {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        return listener != null ? listener.getRecipeManager() : null;
    };

    private Platform() {
    }

    /**
     * Throws an {@link AssertionError}.
     */
    public static <T> T error() {
        throw new AssertionError();
    }

    /**
     * @return Whether the mod is running in a development environment
     */
    @ExpectPlatform
    public static boolean isDevelopment() {
        return Platform.error();
    }

    /**
     * @return Whether the mod is running in a production environment
     */
    @ExpectPlatform
    public static boolean isProduction() {
        return Platform.error();
    }

    /**
     * @return The environment the game is running on
     */
    @ExpectPlatform
    public static Env getEnv() {
        return Platform.error();
    }

    /**
     * @return Whether Optifrick is breaking the game
     */
    @ExpectPlatform
    public static boolean isOptifrickLoaded() {
        return Platform.error();
    }

    /**
     * @return The main game executor. This is the Minecraft client or server instance
     */
    @ExpectPlatform
    public static BlockableEventLoop<?> getGameExecutor() {
        return Platform.error();
    }

    /**
     * @return The current Minecraft version
     */
    public static String getGameVersion() {
        return SharedConstants.getCurrentVersion().getId();
    }

    /**
     * @return The root directory for the current Minecraft instance
     */
    @ExpectPlatform
    public static Path getGameDir() {
        return Platform.error();
    }

    /**
     * @return The config directory for the current Minecraft instance
     */
    @ExpectPlatform
    public static Path getConfigDir() {
        return Platform.error();
    }

    /**
     * @return The mod folder for the current Minecraft instance
     */
    @ExpectPlatform
    public static Path getModsDir() {
        return Platform.error();
    }

    /**
     * Checks to see if the specified mod is loaded.
     *
     * @param modId The mod to check for
     * @return Whether the given mod is loaded
     */
    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        return Platform.error();
    }

    /**
     * Checks to see if the specified mods are loaded.
     *
     * @param modIds The mods to check for
     * @return Whether all the given mods are loaded
     */
    public static boolean areModsLoaded(String... modIds) {
        if (modIds == null) return true;
        for (String modId : modIds) {
            if (!isModLoaded(modId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return A stream of all loaded mods
     */
    @ExpectPlatform
    public static Stream<ModContainer> getMods() {
        return Platform.error();
    }

    /**
     * Gets a {@link ModContainer} using the specified id.
     *
     * @param modId The id of the mod to find
     * @return A mod container if the specified mod is present, otherwise {@link Optional#empty()}
     */
    @ExpectPlatform
    public static Optional<ModContainer> getMod(String modId) {
        return Platform.error();
    }

    // TODO: Properly implement server lifecycle events so these methods can work
    /**
     * @return The currently running Minecraft server. This will not be present in a remote client level
     */
    public static Optional<MinecraftServer> getRunningServer() {
        return Optional.ofNullable(Tesseract.getRunningServer());
    }

    /**
     * @return The access to registries for the running server or client
     */
    public static Optional<RegistryAccess> getRegistryAccess() {
        MinecraftServer server = Tesseract.getRunningServer();
        if (server != null)
            return Optional.of(server.registryAccess());
        return getEnv().isClient() ? Optional.ofNullable(CLIENT_REGISTRY_ACCESS.get()) : Optional.empty();
    }

    /**
     * @return The recipe manager for the running server or client
     */
    public static Optional<RecipeManager> getRecipeManager() {
        MinecraftServer server = Tesseract.getRunningServer();
        if (server != null)
            return Optional.of(server.getRecipeManager());
        return getEnv().isClient() ? Optional.ofNullable(CLIENT_RECIPE_MANAGER.get()) : Optional.empty();
    }

    /**
     * @return Whether the game is running on Forge
     */
    public static boolean isForge() {
        return FORGE;
    }

    /**
     * @return Whether the game is running on Fabric
     */
    public static boolean isFabric() {
        return FABRIC;
    }

    /**
     * A platform-agnostic wrapper for {@code EnvType} and {@code Dist} on their respective platforms.
     *
     * @author ebo2022
     * @since 1.0.0
     */
    public enum Env {

        /**
         * Denotes the game is running on a client.
         */
        CLIENT,

        /**
         * Denotes the game is running on a server.
         */
        SERVER;

        /**
         * Converts platform-specific environment types to {@link Env}.
         *
         * @param type A platform-specific enum member (<code>Dist</code> or <code>EnvType</code>)
         * @return The matching {@link Env} member
         */
        public static Env fromPlatform(Object type) {
            return type == EnvType.CLIENT ? CLIENT : type == EnvType.SERVER ? SERVER : null;
        }

        /**
         * @return Whether the current env is a client
         */
        public boolean isClient() {
            return this == CLIENT;
        }

        /**
         * @return Whether the current env is a server
         */
        public boolean isServer() {
            return this == SERVER;
        }
    }
}

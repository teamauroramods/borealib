package dev.tesseract.api.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import net.fabricmc.api.EnvType;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A mod instance designed to streamline initialization on the common side.
 *
 * @author ebo2022
 * @since 1.0.0
 */
public abstract class Platform {

    private static boolean FORGE = ArchitecturyTarget.getCurrentTarget().equals(PlatformOnly.FORGE);
    private static final Supplier<RegistryAccess> CLIENT_REGISTRY_ACCESS = () -> {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        return listener != null ? listener.registryAccess() : null;
    };
    private static final Supplier<RegistryAccess> CLIENT_RECIPE_MANAGER = () -> {
        ClientPacketListener listener = Minecraft.getInstance().getConnection();
        return listener != null ? listener.registryAccess() : null;
    };
    private final String modId;

    protected Platform(String modId) {
        this.modId = modId;
    }

    /**
     * Creates a {@link Builder} to initialize a mod.
     *
     * @param modId The ID of your mod
     * @return A {@link Builder} to create a mod instance
     */
    public static Builder builder(String modId) {
        return new Builder(modId);
    }

    /**
     * Utility method that throws an {@link AssertionError}.
     */
    public static <T> T error() {
        throw new AssertionError();
    }

    /**
     * @return The Minecraft version this mod is running on
     */
    public static String getMinecraftVersion() {
        return SharedConstants.getCurrentVersion().getId();
    }

    /**
     * @return Whether this mod is running in a production environment
     */
    @ExpectPlatform
    public static boolean isProduction() {
        return Platform.error();
    }

    /**
     * @return Whether this mod is running in a development environment
     */
    @ExpectPlatform
    public static boolean isDevelopment() {
        return Platform.error();
    }

    /**
     * @return The root directory of the current Minecraft instance
     */
    @ExpectPlatform
    public static Path getGameDirectory() {
        return Platform.error();
    }

    /**
     * @return The <code>config</code> folder of the current Minecraft instance
     */
    @ExpectPlatform
    public static Path getConfigFolder() {
        return Platform.error();
    }

    /**
     * @return The <code>mods</code> folder of the current Minecraft instance
     */
    @ExpectPlatform
    public static Path getModsFolder() {
        return Platform.error();
    }

    /**
     * @return The environment this mod is running in, as an instance of {@link Environment}
     */
    @ExpectPlatform
    public static Environment getAgnosticEnvironment() {
        return Platform.error();
    }

    /**
     * @return The environment this mod is running in, as an instance of {@link EnvType} (remapped from <code>Dist</code> if on Forge)
     */
    @ExpectPlatform
    public static EnvType getEnvironment() {
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
     * Checks if the specified mod is loaded.
     *
     * @param modId The ID of the mod to check for
     * @return Whether the mod is loaded
     */
    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        return Platform.error();
    }

    /**
     * Checks if all the specified mods are loaded.
     *
     * @param modIds A list of mod IDs to check for
     * @return Whether all the mods are loaded
     */
    public static boolean areModsLoaded(String... modIds) {
        for (String id : modIds) {
            if (!isModLoaded(id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return A collection of all loaded mod IDs
     */
    @ExpectPlatform
    public static Collection<String> getModIds() {
        return Platform.error();
    }


    /**
     * @return A stream of all loaded mods
     */
    @ExpectPlatform
    public static Stream<Mod> getMods() {
        return Platform.error();
    }

    /**
     * Queries a {@link Mod} container by its ID.
     *
     * @param modId The mod ID to search for
     * @return The mod container, if it was found
     * @throws NoSuchElementException Thrown if no mod with the given ID exists
     */
    @ExpectPlatform
    public static Mod getMod(String modId) {
        return Platform.error();
    }

    /**
     * Optionally queries a {@link Mod} container by its ID if it exists.
     *
     * @param modId The mod ID to search for
     * @return An optional representing the mod container. If the mod isn't present, {@link Optional#empty()} is returned instead
     */
    public static Optional<Mod> getOptionalMod(String modId) {
        try {
            return Optional.of(getMod(modId));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    /**
     * @return Whether the platform is running on Forge
     */
    public static boolean isForge() {
        return FORGE;
    }

    /**
     * @return The mod ID of the platform
     */
    public String getModId() {
        return modId;
    }

    /**
     * Initializes the {@link Platform} instance with all code specified in the builder.
     *
     * <p>On Forge, this should be run at the beginning of the mod constructor.
     * <p>On Fabric, this should be run on the mod initializer.
     * <p><i>Note: Fabric users shouldn't run this on the client initializer. Running this on the main initializer handles client setup as well.</i>
     */
    public abstract void initialize();

    /**
     * Initializes data generation on Fabric.
     * @param generator The data generator to generate data
     */
    @PlatformOnly(PlatformOnly.FABRIC)
    public void initializeData(DataGenerator generator) {
    }

    /**
     * Context for initializing mods during modloading.
     *
     * @author ebo2022
     * @since 1.0.0
     */
    public interface SetupContext {

        /**
         * Queues code to run later when it is safe to do so.
         *
         * @param work The code to run
         * @return A {@link CompletableFuture} for when the code is ran
         */
        CompletableFuture<Void> enqueueWork(Runnable work);

        /**
         * Queues code to run later when it is safe to do so.
         *
         * @param work The code to run
         * @return A {@link CompletableFuture} for when the code is ran
         */
        <T> CompletableFuture<T> enqueueWork(Supplier<T> work);
    }

    /**
     * Context for initializing datagen.
     *
     * @author ebo2022
     * @see net.minecraft.data.DataProvider
     * @since 1.0.0
     */
    public interface DataSetupContext {

        /**
         * @return The data generator to add providers to
         */
        DataGenerator getGenerator();

        /**
         * @return The mod the data generator is running for
         */
        Mod getMod();
    }

    /**
     * A builder to create a {@link Platform} instance.
     *
     * @author ebo2022
     * @since 1.0.0
     */
    public static class Builder {

        private final String modId;
        private Runnable commonInit = () -> {
        };
        private Supplier<Runnable> clientInit = () -> () -> {
        };
        private Supplier<Runnable> serverInit = () -> () -> {
        };
        private Consumer<SetupContext> commonPostInit = __ -> {
        };
        private Supplier<Consumer<SetupContext>> clientPostInit = () -> __ -> {
        };
        private Supplier<Consumer<SetupContext>> serverPostInit = () -> __ -> {
        };
        private Consumer<DataSetupContext> dataInit = __ -> {
        };

        private Builder(String modId) {
            this.modId = modId;
        }

        @ApiStatus.Internal
        @ExpectPlatform
        public static Platform buildImpl(String modId, Runnable commonInit, Supplier<Runnable> clientInit, Supplier<Runnable> serverInit, Consumer<SetupContext> commonPostInit, Supplier<Consumer<SetupContext>> clientPostInit, Supplier<Consumer<SetupContext>> serverPostInit, Consumer<DataSetupContext> dataInit) {
            return Platform.error();
        }

        /**
         * Adds code to be run during the common setup phase.
         */
        public Builder commonInit(Runnable onCommonInit) {
            this.commonInit = onCommonInit;
            return this;
        }

        /**
         * Adds code to be run during the client setup phase.
         */
        public Builder clientInit(Supplier<Runnable> onClientInit) {
            this.clientInit = onClientInit;
            return this;
        }

        /**
         * Adds code to be run during the server setup phase.
         */
        public Builder serverInit(Supplier<Runnable> onServerInit) {
            this.serverInit = onServerInit;
            return this;
        }

        /**
         * Adds code to be run after the common setup phase.
         */
        public Builder commonPostInit(Consumer<SetupContext> onCommonPostInit) {
            this.commonPostInit = onCommonPostInit;
            return this;
        }

        /**
         * Adds code to be run after the client setup phase.
         */
        public Builder clientPostInit(Supplier<Consumer<SetupContext>> onClientPostInit) {
            this.clientPostInit = onClientPostInit;
            return this;
        }

        /**
         * Adds code to be run after the server setup phase.
         */
        public Builder serverPostInit(Supplier<Consumer<SetupContext>> onServerPostInit) {
            this.serverPostInit = onServerPostInit;
            return this;
        }

        /**
         * Adds code to be run during data generation.
         */
        public Builder dataInit(Consumer<DataSetupContext> dataInit) {
            this.dataInit = dataInit;
            return this;
        }

        /**
         * Finishes building the platform instance.
         *
         * @return The built platform
         */
        public Platform build() {
            return buildImpl(this.modId, this.commonInit, this.clientInit, this.serverInit, this.commonPostInit, this.clientPostInit, this.serverPostInit, this.dataInit);
        }
    }

    /**
     * A platform-agnostic wrapper for the <code>EnvType</code> and <code>Dist</code> enums on Forge and Fabric, respectively.
     *
     * @author ebo2022
     * @since 1.0.0
     */
    public enum Environment {
        CLIENT,
        SERVER;

        /**
         * Converts a platform-specific environment enum to a platform-agnostic one.
         *
         * @param type The platform-specific environment enum
         * @return The platform-agnostic environment enum
         */
        public static Environment fromPlatform(Object type) {
            return type == EnvType.CLIENT ? CLIENT : type == EnvType.SERVER ? SERVER : null;
        }

        /**
         * Converts a platform-agnostic environment enum to a platform-specific one.
         *
         * @return The platform-specific environment enum
         */
        public EnvType toPlatform() {
            return this == CLIENT ? EnvType.CLIENT : EnvType.SERVER;
        }
    }
}

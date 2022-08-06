package dev.tesseract.api.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.data.DataGenerator;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ModInstance {

    private final String modId;

    protected ModInstance(String modId) {
        this.modId = modId;
    }

    /**
     * Creates a builder to make a new mod instance.
     *
     * @param modId The id of the mod instance to build
     * @return A new builder
     */
    public static Builder builder(String modId) {
        return new Builder(modId);
    }

    /**
     * @return The id of this mod instance
     */
    public String getModId() {
        return this.modId;
    }

    /**
     * Initializes the {@link ModInstance}.
     *
     * <p>Forge: This should be run in the mod constructor.
     * <p>Fabric: This should be run on the common initializer. Running the method handles client initialization as well.
     */
    public abstract void setup();

    /**
     * Initializes the {@link ModInstance} data generator.
     *
     * <p>Currently for Fabric users only; this should be run on the data initializer.
     */
    @PlatformOnly(PlatformOnly.FABRIC)
    public void dataSetup(DataGenerator dataGenerator) {
    }

    /**
     * Used as context to ensure non-threadsafe code is initialized safely.
     *
     * @author ebo2022
     * @since 1.0.0
     */
    public interface ParallelDispatcher {

        /**
         * Queues work to be run later when it is safe to do so.
         *
         * @param runnable The work to run
         * @return A {@link CompletableFuture} for when the work is done
         */
        CompletableFuture<Void> enqueueWork(Runnable runnable);

        /**
         * Queues work to be run later when it is safe to do so.
         *
         * @param work The work to run
         * @return A {@link CompletableFuture} for when the work is done
         */
        <T> CompletableFuture<T> enqueueWork(Supplier<T> work);
    }

    /**
     * Used as context for initializing data generators.
     *
     * @author ebo2022
     * @since 1.0.0
     */
    public interface DataSetupContext {

        /**
         * @return The data generator to add providers to
         */
        DataGenerator getGenerator();

        /**
         * @return The mod the generator is running for
         */
        ModContainer getMod();
    }

    /**
     * A builder to create a new {@link ModInstance}.
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
        private Consumer<ParallelDispatcher> commonPostInit = __ -> {
        };
        private Supplier<Consumer<ParallelDispatcher>> clientPostInit = () -> __ -> {
        };
        private Supplier<Consumer<ParallelDispatcher>> serverPostInit = () -> __ -> {
        };
        private Consumer<DataSetupContext> dataInit = __ -> {
        };

        private Builder(String modId) {
            this.modId = modId;
        }

        /**
         * Adds code to be run during the common setup phase.
         *
         * @param onCommonInit The code to run
         */
        public Builder commonInit(Runnable onCommonInit) {
            this.commonInit = onCommonInit;
            return this;
        }

        /**
         * Adds code to run during the client setup phase.
         *
         * @param onClientInit The code to run
         */
        public Builder clientInit(Supplier<Runnable> onClientInit) {
            this.clientInit = onClientInit;
            return this;
        }

        /**
         * Adds code to be run during the server setup phase.
         *
         * @param onServerInit The code to run
         */
        public Builder serverInit(Supplier<Runnable> onServerInit) {
            this.serverInit = onServerInit;
            return this;
        }

        /**
         * Adds code to be run after the common setup phase.
         *
         * @param onCommonPostInit The code to run
         */
        public Builder commonPostInit(Consumer<ParallelDispatcher> onCommonPostInit) {
            this.commonPostInit = onCommonPostInit;
            return this;
        }

        /**
         * Adds code to be run after the client setup phase.
         *
         * @param onClientPostInit The code to run
         */
        public Builder clientPostInit(Supplier<Consumer<ParallelDispatcher>> onClientPostInit) {
            this.clientPostInit = onClientPostInit;
            return this;
        }

        /**
         * Adds code to be run after the server setup phase.
         *
         * @param onServerPostInit The code to run
         */
        public Builder serverPostInit(Supplier<Consumer<ParallelDispatcher>> onServerPostInit) {
            this.serverPostInit = onServerPostInit;
            return this;
        }

        /**
         * Adds code to be run during data generation.
         *
         * @param dataInit The code to run
         */
        public Builder dataInit(Consumer<DataSetupContext> dataInit) {
            this.dataInit = dataInit;
            return this;
        }

        /**
         * @return The built {@link ModInstance}
         */
        public ModInstance build() {
            return buildImpl(this.modId, this.commonInit, this.clientInit, this.serverInit, this.commonPostInit, this.clientPostInit, this.serverPostInit, this.dataInit);
        }

        @ApiStatus.Internal
        @ExpectPlatform
        public static ModInstance buildImpl(String modId, Runnable commonInit, Supplier<Runnable> clientInit, Supplier<Runnable> serverInit, Consumer<ParallelDispatcher> commonPostInit, Supplier<Consumer<ParallelDispatcher>> clientPostInit, Supplier<Consumer<ParallelDispatcher>> serverPostInit, Consumer<DataSetupContext> dataInit) {
            return Platform.error();
        }
    }
}

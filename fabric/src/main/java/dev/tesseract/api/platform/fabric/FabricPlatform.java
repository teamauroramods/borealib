package dev.tesseract.api.platform.fabric;

import dev.tesseract.api.platform.Mod;
import dev.tesseract.api.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.data.DataGenerator;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class FabricPlatform extends Platform {
    private final Runnable commonInit;
    private final Supplier<Runnable> clientInit;
    private final Supplier<Runnable> serverInit;
    private final Consumer<SetupContext> commonPostInit;
    private final Supplier<Consumer<Platform.SetupContext>> clientPostInit;
    private final Supplier<Consumer<Platform.SetupContext>> serverPostInit;
    private final Consumer<DataSetupContext> dataInit;

    FabricPlatform(String modId, Runnable commonInit, Supplier<Runnable> clientInit, Supplier<Runnable> serverInit, Consumer<Platform.SetupContext> commonPostInit, Supplier<Consumer<Platform.SetupContext>> clientPostInit, Supplier<Consumer<Platform.SetupContext>> serverPostInit, Consumer<DataSetupContext> dataInit) {
        super(modId);
        this.commonInit = commonInit;
        this.clientInit = clientInit;
        this.serverInit = serverInit;
        this.commonPostInit = commonPostInit;
        this.clientPostInit = clientPostInit;
        this.serverPostInit = serverPostInit;
        this.dataInit = dataInit;
    }

    @Override
    public void initialize() {
        SetupContext context = new SetupContextImpl();

        this.commonInit.run();
        this.commonPostInit.accept(context);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            this.clientInit.get().run();
            this.clientPostInit.get().accept(context);
        } else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            this.serverInit.get().run();
            this.serverPostInit.get().accept(context);
        }
    }

    @Override
    public void initializeData(DataGenerator dataGenerator) {
        this.dataInit.accept(new DataSetupContextImpl((FabricDataGenerator) dataGenerator));
    }

    private static class SetupContextImpl implements SetupContext {

        @Override
        public CompletableFuture<Void> enqueueWork(Runnable work) {
            work.run();
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
            return CompletableFuture.completedFuture(work.get());
        }
    }

    private static class DataSetupContextImpl implements DataSetupContext {

        private final FabricDataGenerator generator;

        private DataSetupContextImpl(FabricDataGenerator generator) {
            this.generator = generator;
        }

        @Override
        public DataGenerator getGenerator() {
            return generator;
        }

        @Override
        public Mod getMod() {
            return new ModImpl(this.generator.getModContainer());
        }
    }
}

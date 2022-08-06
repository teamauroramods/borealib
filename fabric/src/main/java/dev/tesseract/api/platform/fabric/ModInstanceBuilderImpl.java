package dev.tesseract.api.platform.fabric;

import dev.tesseract.api.platform.ModInstance;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
public class ModInstanceBuilderImpl {

    public static ModInstance buildImpl(String modId, Runnable commonInit, Supplier<Runnable> clientInit, Supplier<Runnable> serverInit, Consumer<ModInstance.ParallelDispatcher> commonPostInit, Supplier<Consumer<ModInstance.ParallelDispatcher>> clientPostInit, Supplier<Consumer<ModInstance.ParallelDispatcher>> serverPostInit, Consumer<ModInstance.DataSetupContext> dataInit) {
        return new FabricModInstance(modId, commonInit, clientInit, serverInit, commonPostInit, clientPostInit, serverPostInit, dataInit);
    }
}

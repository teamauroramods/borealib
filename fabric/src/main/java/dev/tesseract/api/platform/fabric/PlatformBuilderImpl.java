package dev.tesseract.api.platform.fabric;

import dev.tesseract.api.platform.Platform;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
public class PlatformBuilderImpl {
    public static Platform buildImpl(String modId, Runnable commonInit, Supplier<Runnable> clientInit, Supplier<Runnable> serverInit, Consumer<Platform.SetupContext> commonPostInit, Supplier<Consumer<Platform.SetupContext>> clientPostInit, Supplier<Consumer<Platform.SetupContext>> serverPostInit, Consumer<Platform.DataSetupContext> dataInit) {
        return new FabricPlatform(modId, commonInit, clientInit, serverInit, commonPostInit, clientPostInit, serverPostInit, dataInit);
    }
}

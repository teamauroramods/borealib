package dev.tesseract.api.platform.forge;

import dev.tesseract.api.platform.ModInstance;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
public class ModInstanceBuilderImpl {

    public static ModInstance buildImpl(String modId, Runnable commonInit, Supplier<Runnable> clientInit, Supplier<Runnable> serverInit, Consumer<ModInstance.ParallelDispatcher> commonPostInit, Supplier<Consumer<ModInstance.ParallelDispatcher>> clientPostInit, Supplier<Consumer<ModInstance.ParallelDispatcher>> serverPostInit, Consumer<ModInstance.DataSetupContext> dataInit) {
        return new ForgeModInstance(modId, FMLJavaModLoadingContext.get().getModEventBus(), commonInit, clientInit, serverInit, commonPostInit, clientPostInit, serverPostInit, dataInit);
    }
}

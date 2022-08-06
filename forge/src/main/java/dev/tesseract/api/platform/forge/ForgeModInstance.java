package dev.tesseract.api.platform.forge;

import dev.tesseract.api.platform.ModContainer;
import dev.tesseract.api.platform.ModInstance;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class ForgeModInstance extends ModInstance {
    private final IEventBus eventBus;
    private final Runnable commonInit;
    private final Supplier<Runnable> clientInit;
    private final Supplier<Runnable> serverInit;
    private final Consumer<ParallelDispatcher> commonPostInit;
    private final Supplier<Consumer<ParallelDispatcher>> clientPostInit;
    private final Supplier<Consumer<ParallelDispatcher>> serverPostInit;
    private final Consumer<DataSetupContext> dataInit;

    ForgeModInstance(String modId, IEventBus eventBus, Runnable commonInit, Supplier<Runnable> clientInit, Supplier<Runnable> serverInit, Consumer<ParallelDispatcher> commonPostInit, Supplier<Consumer<ParallelDispatcher>> clientPostInit, Supplier<Consumer<ParallelDispatcher>> serverPostInit, Consumer<DataSetupContext> dataInit) {
        super(modId);
        this.eventBus = eventBus;
        this.commonInit = commonInit;
        this.clientInit = clientInit;
        this.serverInit = serverInit;
        this.commonPostInit = commonPostInit;
        this.clientPostInit = clientPostInit;
        this.serverPostInit = serverPostInit;
        this.dataInit = dataInit;
    }

    @Override
    public void setup() {
        this.eventBus.<FMLCommonSetupEvent>addListener(e -> this.commonPostInit.accept(new ParallelDispatcherImpl(e)));
        this.eventBus.<FMLClientSetupEvent>addListener(e -> this.clientPostInit.get().accept(new ParallelDispatcherImpl(e)));
        this.eventBus.<FMLDedicatedServerSetupEvent>addListener(e -> this.serverPostInit.get().accept(new ParallelDispatcherImpl(e)));
        this.eventBus.<GatherDataEvent>addListener(e -> this.dataInit.accept(new DataSetupContextImpl(e.getGenerator(), new ModContainerImpl(e.getModContainer()))));

        this.commonInit.run();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.clientInit.get().run());
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> this.serverInit.get().run());
    }

    public IEventBus getEventBus() {
        return eventBus;
    }

    private static class ParallelDispatcherImpl implements ParallelDispatcher {

        private final ParallelDispatchEvent parent;

        private ParallelDispatcherImpl(ParallelDispatchEvent parent) {
            this.parent = parent;
        }

        @Override
        public CompletableFuture<Void> enqueueWork(Runnable work) {
            return this.parent.enqueueWork(work);
        }

        @Override
        public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
            return this.parent.enqueueWork(work);
        }
    }

    private static class DataSetupContextImpl implements DataSetupContext {

        private final DataGenerator dataGenerator;
        private final ModContainer modContainer;

        private DataSetupContextImpl(DataGenerator dataGenerator, ModContainer modContainer) {
            this.dataGenerator = dataGenerator;
            this.modContainer = modContainer;
        }

        @Override
        public DataGenerator getGenerator() {
            return this.dataGenerator;
        }

        @Override
        public ModContainer getMod() {
            return this.modContainer;
        }
    }
}

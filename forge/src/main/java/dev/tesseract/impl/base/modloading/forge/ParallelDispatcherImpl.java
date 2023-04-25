package dev.tesseract.impl.base.modloading.forge;

import dev.tesseract.api.base.v1.modloading.ModLoaderService;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@ApiStatus.Internal
public class ParallelDispatcherImpl implements ModLoaderService.ParallelDispatcher {

    private final ParallelDispatchEvent event;

    public ParallelDispatcherImpl(ParallelDispatchEvent event) {
        this.event = event;
    }

    @Override
    public CompletableFuture<Void> enqueueWork(Runnable work) {
        return this.event.enqueueWork(work);
    }

    @Override
    public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
        return this.event.enqueueWork(work);
    }
}

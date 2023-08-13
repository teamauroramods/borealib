package com.teamaurora.borealib.api.base.v1.modloading.fabric;

import com.teamaurora.borealib.api.base.v1.util.ParallelDispatcher;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class FabricParallelDispatcher implements ParallelDispatcher {

    private static final FabricParallelDispatcher INSTANCE = new FabricParallelDispatcher();

    public static ParallelDispatcher get() {
        return INSTANCE;
    }

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

package com.teamaurora.borealib.api.base.v1.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Wraps enqueue code for Forge's <code>ParallelDispatch</code>-based events. Some code may need to be enqueued to ensure proper thread safety.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface ParallelDispatcher {

    /**
     * Queues work to happen later when it is safe to do so.
     * <p><i>NOTE: The returned future may execute on the current thread so it is not safe to call {@link CompletableFuture#join()} or {@link CompletableFuture#get()}</i>
     *
     * @param work The work to do
     * @return A future for when the work is done
     */
    CompletableFuture<Void> enqueueWork(Runnable work);

    /**
     * Queues work to happen later when it is safe to do so.
     * <p><i>NOTE: The returned future may execute on the current thread so it is not safe to call {@link CompletableFuture#join()} or {@link CompletableFuture#get()}</i>
     *
     * @param work The work to do
     * @return A future for when the work is done
     */
    <T> CompletableFuture<T> enqueueWork(Supplier<T> work);
}
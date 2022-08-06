package dev.tesseract.api.event;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public abstract class Event<T> {

    protected volatile T invoker;

    /**
     * @return The callback for invoking all listeners of this event
     */
    public T invoker() {
        return invoker;
    }

    /**
     * Register a listener to happen in this event.
     *
     * @param listener The listener to add
     */
    public abstract void register(T listener);
}

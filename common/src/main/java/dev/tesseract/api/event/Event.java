package dev.tesseract.api.event;

import org.jetbrains.annotations.ApiStatus;

/**
 * The base interface for a callable event. Based on Architectury's event system.
 *
 * @author ebo2022
 * @since 1.0.0
 * @param <T> The functional interface or EventActor that handles listeners
 */
@ApiStatus.NonExtendable
public interface Event<T> {

    /**
     * @return The callback for invoking this event
     */
    T invoker();

    /**
     * Checks if the specified listener has been registered for this event.
     *
     * @param listener The listener to look for
     * @return Whether the listener has been registered
     */
    boolean isRegistered(T listener);

    /**
     * Registers a listener for this event.
     *
     * @param listener The listener to register
     */
    void register(T listener);
}

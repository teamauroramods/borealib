package dev.tesseract.api.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

/**
 * A class that holds results for an event with the ability to pass on extra data.
 *
 * @param <T> The type of data the result stores
 * @see #pass()
 * @see #interrupt(Boolean, Object)
 * @author ebo2022
 * @since 1.0.0
 */
public final class CompoundEventResult<T> {

    private static final CompoundEventResult<?> PASS = new CompoundEventResult<>(EventResult.pass(), null);

    private final EventResult result;
    private final T object;

    private CompoundEventResult(EventResult result, T object) {
        this.result = result;
        this.object = object;
    }

    /**
     * @return A result that continues on to the next listener and doesn't contain any extra data
     */
    public static <T> CompoundEventResult<T> pass() {
        return (CompoundEventResult<T>) PASS;
    }

    /**
     * Stops the event and prevents it from continuing on to the next listener.
     *
     * @param value The outcome of the event. Passing {@code null} represents the default outcome, which is often falling back to vanilla logic
     * @param object The extra result data, typically the return value of the event
     * @return A result that interrupts the event
     */
    public static <T> CompoundEventResult<T> interrupt(Boolean value, T object) {
        return new CompoundEventResult<>(EventResult.interrupt(value), object);
    }

    /**
     * @return Whether this result prevents other listeners from being processed
     */
    public boolean interruptsListeners() {
        return result.interruptsListeners();
    }

    /**
     * @return The outcome of this result, returns {@code null} if none is present
     */
    public Boolean getValue() {
        return result.getValue();
    }

    /**
     * @return Whether this result has no outcome
     */
    public boolean isEmpty() {
        return result.isEmpty();
    }

    /**
     * @return Whether this result has an outcome
     */
    public boolean isPresent() {
        return result.isPresent();
    }

    /**
     * @return Whether this result has a {@code true} outcome
     */
    public boolean isTrue() {
        return result.isTrue();
    }

    /**
     * @return Whether this result has a {@code false} outcome
     */
    public boolean isFalse() {
        return result.isFalse();
    }

    /**
     * @return The {@link EventResult} without extra data attached
     */
    public EventResult getResult() {
        return result;
    }

    /**
     * @return The extra data passed on with this result
     */
    public T getObject() {
        return object;
    }

    /**
     * @return This result, converted to a vanilla {@link InteractionResult}
     */
    public InteractionResultHolder<T> asInteraction() {
        return new InteractionResultHolder<>(result.asInteraction(), object);
    }
}

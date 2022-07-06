package dev.tesseract.api.event;

import net.minecraft.world.InteractionResult;
import org.apache.commons.lang3.BooleanUtils;

/**
 * A class that holds results for an event.
 *
 * @see #pass()
 * @see #interrupt(Boolean)
 * @see CompoundEventResult
 * @author ebo2022
 * @since 1.0.0
 */
public final class EventResult {

    private static final EventResult TRUE = new EventResult(true, true);
    private static final EventResult STOP = new EventResult(true, null);
    private static final EventResult PASS = new EventResult(false, null);
    private static final EventResult FALSE = new EventResult(true, false);

    private final boolean interruptsListeners;
    private final Boolean value;

     private EventResult(boolean interruptsFurtherEvaluation, Boolean value) {
        this.interruptsListeners = interruptsFurtherEvaluation;
        this.value = value;
    }

    /**
     * @return A result that continues on to the next listener
     */
    public static EventResult pass() {
        return PASS;
    }


    /**
     * Stops the event and prevents it from continuing on to the next listener.
     *
     * @param value The outcome of the event. Passing {@code null} represents the default outcome, which is often falling back to vanilla logic
     * @return A result that interrupts the event
     */
    public static EventResult interrupt(Boolean value) {
        if (value == null) return STOP;
        if (value) return TRUE;
        return FALSE;
    }

    /**
     * @return Whether this result prevents other listeners from being processed
     */
    public boolean interruptsListeners() {
        return interruptsListeners;
    }

    /**
     * @return The outcome of this result, returns {@code null} if none is present
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * @return Whether this result has no outcome
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * @return Whether this result has an outcome
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * @return Whether this result has a {@code true} outcome
     */
    public boolean isTrue() {
        return BooleanUtils.isTrue(value);
    }

    /**
     * @return Whether this result has a {@code false} outcome
     */
    public boolean isFalse() {
        return BooleanUtils.isFalse(value);
    }

    /**
     * @return This result, converted to a vanilla {@link InteractionResult}
     */
    public InteractionResult asInteraction() {
        if (isPresent()) {
            return getValue() ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
}

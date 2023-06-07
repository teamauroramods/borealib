package com.teamaurora.borealib.api.base.v1.event;

import com.teamaurora.borealib.impl.base.event.EventImpl;
import net.minecraft.world.InteractionResult;
import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Function;

/**
 * A simple listenable event.
 *
 * @param <T> The event type. This is intended to be a functional interface
 */
@ApiStatus.NonExtendable
public abstract class Event<T> {

    public static <T> Event<T> create(Class<? super T> type, Function<T[], T> invokerFactory) {
        return new EventImpl<>(type, invokerFactory);
    }

    @SuppressWarnings({"unchecked", "SuspiciousInvocationHandlerImplementation"})
    public static <T> Event<T> createLoop(Class<? super T> type) {
        return create(type, events -> (T) Proxy.newProxyInstance(Event.class.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            for (Object event : events) {
                invokeFast(event, method, args);
            }
            return null;
        }));
    }

    @SuppressWarnings({"unchecked", "SuspiciousInvocationHandlerImplementation"})
    public static <T> Event<T> createResult(Class<? super T> type) {
        return create(type, events -> (T) Proxy.newProxyInstance(Event.class.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            for (Object event : events) {
                InteractionResult result = invokeFast(event, method, args);
                if (result != InteractionResult.PASS) {
                    return result;
                }
            }
            return InteractionResult.PASS;
        }));
    }

    @SuppressWarnings({"unchecked", "SuspiciousInvocationHandlerImplementation"})
    public static <T> Event<T> createCancellable(Class<? super T> type) {
        return create(type, events -> (T) Proxy.newProxyInstance(Event.class.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            for (Object event : events) {
                boolean result = invokeFast(event, method, args);
                if (!result) {
                    return false;
                }
            }
            return true;
        }));
    }

    @SuppressWarnings("unchecked")
    private static <T, S> S invokeFast(T object, Method method, Object[] args) throws Throwable {
        return (S) MethodHandles.lookup().unreflect(method).bindTo(object).invokeWithArguments(args);
    }


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
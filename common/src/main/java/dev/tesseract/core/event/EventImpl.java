package dev.tesseract.core.event;

import dev.tesseract.api.event.Event;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

@ApiStatus.Internal
public class EventImpl<T> implements Event<T> {
    private final Function<List<T>, T> factory;
    private volatile T invoker;
    private final Lock lock = new ReentrantLock();
    private ArrayList<T> listeners;

    public EventImpl(Function<List<T>, T> factory) {
        this.factory = factory;
        this.listeners = new ArrayList<>();
    }

    @Override
    public T invoker() {
        return invoker;
    }

    @Override
    public void register(T listener) {
        Objects.requireNonNull(listener, "Tried to register a null listener");
        this.lock.lock();
        try {
            this.listeners.add(listener);
            this.invoker = this.factory.apply(this.listeners);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean isRegistered(T listener) {
        return listeners.contains(listener);
    }
}

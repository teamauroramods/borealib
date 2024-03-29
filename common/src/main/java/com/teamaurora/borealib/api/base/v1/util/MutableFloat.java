package com.teamaurora.borealib.api.base.v1.util;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Stores a float value and allows for it to be retrieved and modified.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface MutableFloat {

    /**
     * @return A new {@link MutableFloat} initialized with the specified value
     */
    static MutableFloat of(float value) {
        return new DefaultImpl(value);
    }

    /**
     * Creates a {@link MutableFloat} designed to manipulate Forge event parameters.
     *
     * @param provider      The object used to retrieve and set the stored value
     * @param valueSupplier A function to retrieve the value from the provider
     * @param valueSetter   A consumer to externally modify the stored value
     * @return A new {@link MutableInt} with the specified provider class
     */
    static <T> MutableFloat linkToForge(T provider, Function<T, Float> valueSupplier, BiConsumer<T, Float> valueSetter) {
        return new ForgeImpl<>(provider, valueSupplier, valueSetter);
    }

    float getAsFloat();

    void accept(float value);

    class DefaultImpl implements MutableFloat {

        private float value;

        private DefaultImpl(float value) {
            this.accept(value);
        }

        @Override
        public void accept(float value) {
            this.value = value;
        }

        @Override
        public float getAsFloat() {
            return this.value;
        }
    }

    class ForgeImpl<T> implements MutableFloat {

        private final T provider;
        private final Function<T, Float> valueSupplier;
        private final BiConsumer<T, Float> valueSetter;

        private ForgeImpl(T provider, Function<T, Float> valueSupplier, BiConsumer<T, Float> valueSetter) {
            this.provider = provider;
            this.valueSupplier = valueSupplier;
            this.valueSetter = valueSetter;
        }

        @Override
        public void accept(float value) {
            this.valueSetter.accept(this.provider, value);
        }

        @Override
        public float getAsFloat() {
            return this.valueSupplier.apply(this.provider);
        }
    }
}
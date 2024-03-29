package com.teamaurora.borealib.api.base.v1.util;

import net.minecraft.util.StringRepresentable;

import java.util.function.BiPredicate;

public enum NumberComparator implements BiPredicate<Number, Number>, StringRepresentable {

    GREATER_THAN(">", (t, u) -> t.doubleValue() > u.doubleValue()),
    LESS_THAN("<", (t, u) -> t.doubleValue() < u.doubleValue()),
    GREATER_THAN_OR_EQUAL(">=", (t, u) -> t.doubleValue() >= u.doubleValue()),
    LESS_THAN_OR_EQUAL("<=", (t, u) -> t.doubleValue() <= u.doubleValue()),
    EQUAL("=", (t, u) -> t.doubleValue() == u.doubleValue());

    private final String symbol;
    private final BiPredicate<Number, Number> comparator;
    public static final StringRepresentable.EnumCodec<NumberComparator> CODEC = StringRepresentable.fromEnum(NumberComparator::values);

    NumberComparator(String symbol, BiPredicate<Number, Number> comparator) {
        this.symbol = symbol;
        this.comparator = comparator;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getSerializedName() {
        return this.getSymbol();
    }

    @Override
    public boolean test(Number t, Number u) {
        return this.comparator.test(t, u);
    }
}
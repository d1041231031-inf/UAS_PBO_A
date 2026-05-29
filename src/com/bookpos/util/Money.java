package com.bookpos.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class Money {
    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));

    private Money() {
    }

    public static String rupiah(BigDecimal value) {
        return FORMATTER.format(value);
    }
}

package com.aiexpensetracker.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CalculationUtils {

    private CalculationUtils() {
    }

    public static BigDecimal calculatePercentage(
            BigDecimal value,
            BigDecimal total
    ) {

        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return value
                .multiply(BigDecimal.valueOf(100))
                .divide(total, 2, RoundingMode.HALF_UP);
    }
}
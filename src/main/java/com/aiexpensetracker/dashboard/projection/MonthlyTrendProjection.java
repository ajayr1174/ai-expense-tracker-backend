package com.aiexpensetracker.dashboard.projection;

import java.math.BigDecimal;

public interface MonthlyTrendProjection {

    Integer getMonth();

    Integer getYear();

    BigDecimal getAmount();
}

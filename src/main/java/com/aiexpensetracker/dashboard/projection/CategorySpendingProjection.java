package com.aiexpensetracker.dashboard.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface CategorySpendingProjection {

    UUID getCategoryId();

    String getCategoryName();

    String getColor();

    BigDecimal getAmount();
}
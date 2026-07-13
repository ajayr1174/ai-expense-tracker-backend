package com.aiexpensetracker.dashboard.dto.response;

import java.math.BigDecimal;

public record BudgetSummaryResponse(

        BigDecimal totalBudget,

        BigDecimal spentAmount,

        BigDecimal remainingAmount,

        BigDecimal usagePercentage

) {}

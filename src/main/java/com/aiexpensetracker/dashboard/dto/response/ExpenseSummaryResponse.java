package com.aiexpensetracker.dashboard.dto.response;

import java.math.BigDecimal;

public record ExpenseSummaryResponse(
        BigDecimal totalExpense,
        BigDecimal monthlyExpense,
        BigDecimal todayExpense,
        long transactionCount
) {
}
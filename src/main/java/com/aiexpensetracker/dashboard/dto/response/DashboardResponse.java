package com.aiexpensetracker.dashboard.dto.response;

import java.util.List;

public record DashboardResponse(

        DashboardPeriodResponse period,

        ExpenseSummaryResponse expenseSummary,

        BudgetSummaryResponse budgetSummary,

        List<CategorySpendingResponse> categoryBreakdown,

        List<MonthlyTrendResponse> monthlyTrend,

        List<RecentTransactionResponse> recentTransactions

) {}
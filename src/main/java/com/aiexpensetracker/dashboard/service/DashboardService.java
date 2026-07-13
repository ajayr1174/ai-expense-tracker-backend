package com.aiexpensetracker.dashboard.service;

import com.aiexpensetracker.dashboard.dto.response.ExpenseSummaryResponse;

public interface DashboardService {

    ExpenseSummaryResponse getExpenseSummary(
            Integer month,
            Integer year
    );
}

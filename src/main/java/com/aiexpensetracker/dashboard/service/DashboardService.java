package com.aiexpensetracker.dashboard.service;

import com.aiexpensetracker.dashboard.dto.response.*;

import java.util.List;

public interface DashboardService {

    DashboardResponse getDashboard(
            Integer month,
            Integer year
    );
}

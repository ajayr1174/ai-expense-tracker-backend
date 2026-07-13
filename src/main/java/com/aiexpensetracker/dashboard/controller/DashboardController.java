package com.aiexpensetracker.dashboard.controller;

import com.aiexpensetracker.dashboard.dto.response.DashboardResponse;
import com.aiexpensetracker.dashboard.dto.response.ExpenseSummaryResponse;
import com.aiexpensetracker.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummaryResponse> getExpenseSummary(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {

        return ResponseEntity.ok(
                dashboardService.getExpenseSummary(
                        month,
                        year
                )
        );
    }
}
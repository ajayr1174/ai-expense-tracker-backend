package com.aiexpensetracker.dashboard.dto.response;

import java.math.BigDecimal;

public record MonthlyTrendResponse(

        Integer month,

        Integer year,

        BigDecimal amount

) {}

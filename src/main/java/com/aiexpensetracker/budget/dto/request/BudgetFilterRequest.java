package com.aiexpensetracker.budget.dto.request;

import com.aiexpensetracker.budget.enums.BudgetPeriodType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BudgetFilterRequest {

    private UUID categoryId;

    private BudgetPeriodType periodType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean active;

    private String search;
}
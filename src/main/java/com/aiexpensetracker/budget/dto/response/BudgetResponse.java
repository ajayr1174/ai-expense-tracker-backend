package com.aiexpensetracker.budget.dto.response;

import com.aiexpensetracker.budget.enums.BudgetPeriodType;
import com.aiexpensetracker.budget.enums.BudgetStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponse {

    private UUID id;

    private String name;

    private BigDecimal amount;

    private UUID categoryId;

    private String categoryName;

    private BudgetPeriodType periodType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer alertThreshold;

    private BigDecimal spentAmount;

    private BigDecimal remainingAmount;

    private BigDecimal usagePercentage;

    private BudgetStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
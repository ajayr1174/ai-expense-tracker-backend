package com.aiexpensetracker.budget.dto.request;

import com.aiexpensetracker.budget.enums.BudgetPeriodType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BudgetUpdateRequest {

    @NotBlank(message = "Budget name is required")
    @Size(max = 100)
    private String name;

    @NotNull(message = "Budget amount is required")
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    private UUID categoryId;

    @NotNull(message = "Budget period type is required")
    private BudgetPeriodType periodType;

    private LocalDate startDate;

    private LocalDate endDate;

    @Min(1)
    @Max(100)
    private Integer alertThreshold;
}

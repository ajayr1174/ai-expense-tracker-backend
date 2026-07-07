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
public class BudgetCreateRequest {

    @NotBlank(message = "Budget name is required")
    @Size(max = 100, message = "Budget name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Budget amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Budget amount must be greater than zero"
    )
    private BigDecimal amount;

    private UUID categoryId;

    @NotNull(message = "Budget period type is required")
    private BudgetPeriodType periodType;

    private LocalDate startDate;

    private LocalDate endDate;

    @Min(value = 1, message = "Alert threshold must be at least 1")
    @Max(value = 100, message = "Alert threshold cannot exceed 100")
    private Integer alertThreshold = 80;
}

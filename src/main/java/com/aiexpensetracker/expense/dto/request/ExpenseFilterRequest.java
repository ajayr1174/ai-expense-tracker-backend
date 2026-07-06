package com.aiexpensetracker.expense.dto.request;

import com.aiexpensetracker.expense.enums.PaymentMethod;
import com.aiexpensetracker.expense.enums.SortDirection;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseFilterRequest(

        LocalDate startDate,

        LocalDate endDate,

        UUID categoryId,

        PaymentMethod paymentMethod,

        @DecimalMin(value = "0.00")
        BigDecimal minAmount,

        @DecimalMin(value = "0.00")
        BigDecimal maxAmount,

        String search,

        @Min(0)
        Integer page,

        @Min(1)
        Integer size,

        String sortBy,

        SortDirection sortDirection
) {

    public ExpenseFilterRequest {

        page = page == null ? 0 : page;
        size = size == null ? 20 : Math.min(size, 100);

        sortBy = sortBy == null || sortBy.isBlank()
                ? "expenseDate"
                : sortBy;

        sortDirection = sortDirection == null
                ? SortDirection.DESC
                : sortDirection;
    }
}
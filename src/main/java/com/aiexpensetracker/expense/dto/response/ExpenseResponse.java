package com.aiexpensetracker.expense.dto.response;

import com.aiexpensetracker.expense.enums.ExpenseSource;
import com.aiexpensetracker.expense.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponse(

        UUID id,

        UUID categoryId,

        String categoryName,

        BigDecimal amount,

        String currency,

        String description,

        LocalDate expenseDate,

        PaymentMethod paymentMethod,

        ExpenseSource source,

        String merchantName,

        String notes,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
package com.aiexpensetracker.dashboard.dto.response;

import com.aiexpensetracker.expense.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RecentTransactionResponse(

        UUID expenseId,

        UUID categoryId,

        String categoryName,

        String color,

        BigDecimal amount,

        String description,

        LocalDate expenseDate,

        PaymentMethod paymentMethod

) {
}
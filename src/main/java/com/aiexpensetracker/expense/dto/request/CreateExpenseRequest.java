package com.aiexpensetracker.expense.dto.request;

import com.aiexpensetracker.expense.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateExpenseRequest(

        @NotNull(message = "Category is required")
        UUID categoryId,

        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "0.01",
                message = "Amount must be greater than zero"
        )
        BigDecimal amount,

        @NotBlank(message = "Description is required")
        @Size(
                max = 255,
                message = "Description cannot exceed 255 characters"
        )
        String description,

        @NotNull(message = "Expense date is required")
        LocalDate expenseDate,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @Size(max = 150)
        String merchantName,

        @Size(max = 1000)
        String notes
) {
}
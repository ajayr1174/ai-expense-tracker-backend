package com.aiexpensetracker.expense.dto.request;

import com.aiexpensetracker.expense.enums.PaymentMethod;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateExpenseRequest(

        @NotNull(message = "Category is required")
        UUID categoryId,

        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "0.01",
                message = "Amount must be greater than zero"
        )
        BigDecimal amount,

        @NotBlank(message = "Description is required")
        @Size(max = 255)
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
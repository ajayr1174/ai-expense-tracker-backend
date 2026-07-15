package com.aiexpensetracker.dashboard.projection;

import com.aiexpensetracker.expense.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface RecentTransactionProjection {

    UUID getExpenseId();

    UUID getCategoryId();

    String getCategoryName();

    String getCategoryColor();

    BigDecimal getAmount();

    String getDescription();

    LocalDate getExpenseDate();

    PaymentMethod getPaymentMethod();
}
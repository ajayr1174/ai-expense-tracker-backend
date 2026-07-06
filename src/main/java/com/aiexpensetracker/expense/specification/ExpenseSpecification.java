package com.aiexpensetracker.expense.specification;

import com.aiexpensetracker.expense.dto.request.ExpenseFilterRequest;
import com.aiexpensetracker.expense.entity.Expense;
import com.aiexpensetracker.expense.enums.PaymentMethod;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class ExpenseSpecification {

    private ExpenseSpecification() {
    }

    public static Specification<Expense> withFilters(
            UUID userId,
            ExpenseFilterRequest filter
    ) {

        return Specification.allOf(
                belongsToUser(userId),
                expenseDateFrom(filter.startDate()),
                expenseDateTo(filter.endDate()),
                hasCategory(filter.categoryId()),
                hasPaymentMethod(filter.paymentMethod()),
                amountGreaterThanOrEqualTo(filter.minAmount()),
                amountLessThanOrEqualTo(filter.maxAmount()),
                descriptionContains(filter.search())
        );
    }

    private static Specification<Expense> belongsToUser(
            UUID userId
    ) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("user").get("id"),
                        userId
                );
    }

    private static Specification<Expense> expenseDateFrom(
            LocalDate startDate
    ) {

        return (root, query, cb) -> {

            if (startDate == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(
                    root.get("expenseDate"),
                    startDate
            );
        };
    }

    private static Specification<Expense> expenseDateTo(
            LocalDate endDate
    ) {

        return (root, query, cb) -> {

            if (endDate == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(
                    root.get("expenseDate"),
                    endDate
            );
        };
    }

    private static Specification<Expense> hasCategory(
            UUID categoryId
    ) {

        return (root, query, cb) -> {

            if (categoryId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("category").get("id"),
                    categoryId
            );
        };
    }

    private static Specification<Expense> hasPaymentMethod(
            PaymentMethod paymentMethod
    ) {

        return (root, query, cb) -> {

            if (paymentMethod == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("paymentMethod"),
                    paymentMethod
            );
        };
    }

    private static Specification<Expense> amountGreaterThanOrEqualTo(
            BigDecimal minAmount
    ) {

        return (root, query, cb) -> {

            if (minAmount == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(
                    root.get("amount"),
                    minAmount
            );
        };
    }

    private static Specification<Expense> amountLessThanOrEqualTo(
            BigDecimal maxAmount
    ) {

        return (root, query, cb) -> {

            if (maxAmount == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(
                    root.get("amount"),
                    maxAmount
            );
        };
    }

    private static Specification<Expense> descriptionContains(
            String search
    ) {

        return (root, query, cb) -> {

            if (search == null || search.isBlank()) {
                return cb.conjunction();
            }

            String pattern =
                    "%" + search.trim().toLowerCase() + "%";

            return cb.like(
                    cb.lower(root.get("description")),
                    pattern
            );
        };
    }
}

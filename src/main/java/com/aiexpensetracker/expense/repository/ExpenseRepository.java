package com.aiexpensetracker.expense.repository;

import com.aiexpensetracker.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository
        extends JpaRepository<Expense, UUID>,
        JpaSpecificationExecutor<Expense> {
    List<Expense> findAllByUserIdOrderByExpenseDateDesc(UUID userId);

    Optional<Expense> findByIdAndUserId(
            UUID expenseId,
            UUID userId
    );

    boolean existsByIdAndUserId(
            UUID expenseId,
            UUID userId
    );

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.id = :userId
          AND e.expenseDate BETWEEN :startDate AND :endDate
        """)
    BigDecimal sumAmountByUserAndDateRange(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.id = :userId
          AND e.category.id = :categoryId
          AND e.expenseDate BETWEEN :startDate AND :endDate
        """)
    BigDecimal sumAmountByUserAndCategoryAndDateRange(
            UUID userId,
            UUID categoryId,
            LocalDate startDate,
            LocalDate endDate
    );

}
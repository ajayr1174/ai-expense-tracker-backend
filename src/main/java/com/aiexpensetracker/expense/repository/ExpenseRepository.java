package com.aiexpensetracker.expense.repository;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import com.aiexpensetracker.dashboard.projection.CategorySpendingProjection;
import com.aiexpensetracker.dashboard.projection.MonthlyTrendProjection;
import com.aiexpensetracker.dashboard.projection.RecentTransactionProjection;
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


    @Query("""
    SELECT COALESCE(SUM(e.amount), 0)
    FROM Expense e
    WHERE e.user.id = :userId
    """)
    BigDecimal sumTotalAmountByUserId(UUID userId);

    @Query("""
    SELECT COALESCE(SUM(e.amount), 0)
    FROM Expense e
    WHERE e.user.id = :userId
      AND e.expenseDate = :date
    """)
    BigDecimal sumAmountByUserAndDate(
            UUID userId,
            LocalDate date
    );

    long countByUserIdAndExpenseDateBetween(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
    SELECT
        c.id AS categoryId,
        c.name AS categoryName,
        c.color AS color,
        SUM(e.amount) AS amount
    FROM Expense e
    JOIN e.category c
    WHERE e.user.id = :userId
      AND e.expenseDate BETWEEN :startDate AND :endDate
    GROUP BY
        c.id,
        c.name,
        c.color
    ORDER BY SUM(e.amount) DESC
    """)
    List<CategorySpendingProjection> findCategoryBreakdown(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Expense> findTop5ByUserIdOrderByExpenseDateDescCreatedAtDesc(
            UUID userId
    );

    @Query("""
    SELECT
        e.id AS expenseId,
        c.id AS categoryId,
        c.name AS categoryName,
        c.color AS categoryColor,
        e.amount AS amount,
        e.description AS description,
        e.expenseDate AS expenseDate,
        e.paymentMethod AS paymentMethod
    FROM Expense e
    JOIN e.category c
    WHERE e.user.id = :userId
    ORDER BY
        e.expenseDate DESC,
        e.createdAt DESC
    LIMIT 5
    """)
    List<RecentTransactionProjection> findRecentTransactions(
            UUID userId
    );


    @Query("""
    SELECT
        EXTRACT(YEAR FROM e.expenseDate) AS year,
        EXTRACT(MONTH FROM e.expenseDate) AS month,
        SUM(e.amount) AS amount
    FROM Expense e
    WHERE e.user.id = :userId
      AND e.expenseDate BETWEEN :startDate AND :endDate
    GROUP BY
        EXTRACT(YEAR FROM e.expenseDate),
        EXTRACT(MONTH FROM e.expenseDate)
    ORDER BY
        EXTRACT(YEAR FROM e.expenseDate),
        EXTRACT(MONTH FROM e.expenseDate)
    """)
    List<MonthlyTrendProjection> findMonthlyTrend(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    );
}
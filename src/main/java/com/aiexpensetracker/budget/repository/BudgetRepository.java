package com.aiexpensetracker.budget.repository;

import com.aiexpensetracker.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID>, JpaSpecificationExecutor<Budget> {

    Optional<Budget> findByIdAndUserId(
            UUID budgetId,
            UUID userId
    );

    boolean existsByUserIdAndCategoryIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            UUID userId,
            UUID categoryId,
            LocalDate endDate,
            LocalDate startDate
    );

    @Query("""
            SELECT COUNT(b) > 0
            FROM Budget b
            WHERE b.user.id = :userId
              AND b.category IS NULL
              AND b.startDate <= :endDate
              AND b.endDate >= :startDate
            """)
    boolean existsOverlappingOverallBudget(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
    SELECT b
    FROM Budget b
    WHERE b.user.id = :userId
      AND b.category IS NULL
      AND b.startDate <= :date
      AND b.endDate >= :date
    """)
    Optional<Budget> findActiveOverallBudget(
            UUID userId,
            LocalDate date
    );
}
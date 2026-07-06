package com.aiexpensetracker.expense.repository;

import com.aiexpensetracker.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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


}
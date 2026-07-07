package com.aiexpensetracker.budget.service;

import com.aiexpensetracker.budget.dto.request.BudgetCreateRequest;
import com.aiexpensetracker.budget.dto.request.BudgetFilterRequest;
import com.aiexpensetracker.budget.dto.request.BudgetUpdateRequest;
import com.aiexpensetracker.budget.dto.response.BudgetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BudgetService {

    BudgetResponse createBudget(
            BudgetCreateRequest request
    );

    BudgetResponse getBudgetById(
            UUID budgetId
    );

    Page<BudgetResponse> getBudgets(
            BudgetFilterRequest filter,
            Pageable pageable
    );

    BudgetResponse updateBudget(
            UUID budgetId,
            BudgetUpdateRequest request
    );

    void deleteBudget(
            UUID budgetId
    );
}

package com.aiexpensetracker.budget.controller;

import com.aiexpensetracker.budget.dto.request.BudgetCreateRequest;
import com.aiexpensetracker.budget.dto.request.BudgetFilterRequest;
import com.aiexpensetracker.budget.dto.request.BudgetUpdateRequest;
import com.aiexpensetracker.budget.dto.response.BudgetResponse;
import com.aiexpensetracker.budget.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponse createBudget(
            @Valid
            @RequestBody
            BudgetCreateRequest request
    ) {

        return budgetService.createBudget(request);
    }

    @GetMapping("/{budgetId}")
    public BudgetResponse getBudgetById(
            @PathVariable UUID budgetId
    ) {

        return budgetService.getBudgetById(budgetId);
    }

    @GetMapping
    public Page<BudgetResponse> getBudgets(
            @ModelAttribute BudgetFilterRequest filter,

            @PageableDefault(
                    size = 20,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        return budgetService.getBudgets(
                filter,
                pageable
        );
    }

    @PutMapping("/{budgetId}")
    public BudgetResponse updateBudget(
            @PathVariable UUID budgetId,

            @Valid
            @RequestBody
            BudgetUpdateRequest request
    ) {

        return budgetService.updateBudget(
                budgetId,
                request
        );
    }

    @DeleteMapping("/{budgetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudget(
            @PathVariable UUID budgetId
    ) {

        budgetService.deleteBudget(budgetId);
    }
}
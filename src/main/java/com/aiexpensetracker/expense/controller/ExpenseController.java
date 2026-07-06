package com.aiexpensetracker.expense.controller;

import com.aiexpensetracker.common.dto.PageResponse;
import com.aiexpensetracker.expense.dto.request.CreateExpenseRequest;
import com.aiexpensetracker.expense.dto.request.ExpenseFilterRequest;
import com.aiexpensetracker.expense.dto.request.UpdateExpenseRequest;
import com.aiexpensetracker.expense.dto.response.ExpenseResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aiexpensetracker.expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody CreateExpenseRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(expenseService.createExpense(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ExpenseResponse>> getExpenses(
            @Valid @ModelAttribute ExpenseFilterRequest filter
    ) {

        return ResponseEntity.ok(
                expenseService.getExpenses(filter)
        );
    }
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @PathVariable UUID expenseId
    ) {

        return ResponseEntity.ok(
                expenseService.getExpenseById(expenseId)
        );
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable UUID expenseId,
            @Valid @RequestBody UpdateExpenseRequest request
    ) {

        return ResponseEntity.ok(
                expenseService.updateExpense(
                        expenseId,
                        request
                )
        );
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable UUID expenseId
    ) {

        expenseService.deleteExpense(expenseId);

        return ResponseEntity.noContent().build();
    }
}
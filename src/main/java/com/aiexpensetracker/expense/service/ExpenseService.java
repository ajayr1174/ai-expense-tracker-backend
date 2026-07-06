package com.aiexpensetracker.expense.service;


import com.aiexpensetracker.common.dto.PageResponse;
import com.aiexpensetracker.expense.dto.request.CreateExpenseRequest;
import com.aiexpensetracker.expense.dto.request.ExpenseFilterRequest;
import com.aiexpensetracker.expense.dto.request.UpdateExpenseRequest;
import com.aiexpensetracker.expense.dto.response.ExpenseResponse;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    ExpenseResponse createExpense(
            CreateExpenseRequest request
    );

    PageResponse<ExpenseResponse> getExpenses(
            ExpenseFilterRequest filter
    );

    ExpenseResponse getExpenseById(
            UUID expenseId
    );

    ExpenseResponse updateExpense(
            UUID expenseId,
            UpdateExpenseRequest request
    );

    void deleteExpense(
            UUID expenseId
    );
}
package com.aiexpensetracker.expense.service.Impl;

import com.aiexpensetracker.category.entity.Category;
import com.aiexpensetracker.category.repository.CategoryRepository;
import com.aiexpensetracker.common.dto.PageResponse;
import com.aiexpensetracker.exception.BusinessException;
import com.aiexpensetracker.exception.ResourceNotFoundException;
import com.aiexpensetracker.expense.dto.request.CreateExpenseRequest;
import com.aiexpensetracker.expense.dto.request.ExpenseFilterRequest;
import com.aiexpensetracker.expense.dto.request.UpdateExpenseRequest;
import com.aiexpensetracker.expense.dto.response.ExpenseResponse;
import com.aiexpensetracker.expense.entity.Expense;
import com.aiexpensetracker.expense.enums.ExpenseSource;
import com.aiexpensetracker.expense.enums.SortDirection;
import com.aiexpensetracker.expense.mapper.ExpenseMapper;
import com.aiexpensetracker.expense.repository.ExpenseRepository;
import com.aiexpensetracker.expense.service.ExpenseService;
import com.aiexpensetracker.expense.specification.ExpenseSpecification;
import com.aiexpensetracker.security.service.CurrentUserService;
import com.aiexpensetracker.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseMapper expenseMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public ExpenseResponse createExpense(
            CreateExpenseRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );

        Expense expense = Expense.builder()
                .user(currentUser)
                .category(category)
                .amount(request.amount())
                .currency("INR")
                .description(request.description())
                .expenseDate(request.expenseDate())
                .paymentMethod(request.paymentMethod())
                .source(ExpenseSource.MANUAL)
                .merchantName(request.merchantName())
                .notes(request.notes())
                .build();

        Expense savedExpense =
                expenseRepository.save(expense);

        return expenseMapper.toResponse(savedExpense);
    }

    @Override
    public PageResponse<ExpenseResponse> getExpenses(
            ExpenseFilterRequest filter
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        validateFilter(filter);

        Sort sort = createSort(filter);

        Pageable pageable = PageRequest.of(
                filter.page(),
                filter.size(),
                sort
        );

        Specification<Expense> specification =
                ExpenseSpecification.withFilters(
                        currentUser.getId(),
                        filter
                );

        Page<Expense> expensePage =
                expenseRepository.findAll(
                        specification,
                        pageable
                );

        return new PageResponse<>(
                expenseMapper.toResponseList(
                        expensePage.getContent()
                ),
                expensePage.getNumber(),
                expensePage.getSize(),
                expensePage.getTotalElements(),
                expensePage.getTotalPages(),
                expensePage.isFirst(),
                expensePage.isLast()
        );
    }
    @Override
    public ExpenseResponse getExpenseById(
            UUID expenseId
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Expense expense = getUserExpense(
                expenseId,
                currentUser.getId()
        );

        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpense(
            UUID expenseId,
            UpdateExpenseRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Expense expense = getUserExpense(
                expenseId,
                currentUser.getId()
        );

        Category category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );

        expense.setCategory(category);
        expense.setAmount(request.amount());
        expense.setDescription(request.description());
        expense.setExpenseDate(request.expenseDate());
        expense.setPaymentMethod(request.paymentMethod());
        expense.setMerchantName(request.merchantName());
        expense.setNotes(request.notes());

        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional
    public void deleteExpense(UUID expenseId) {

        User currentUser =
                currentUserService.getCurrentUser();

        Expense expense = getUserExpense(
                expenseId,
                currentUser.getId()
        );

        expenseRepository.delete(expense);
    }

    private Expense getUserExpense(
            UUID expenseId,
            UUID userId
    ) {

        return expenseRepository
                .findByIdAndUserId(expenseId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expense not found"
                        )
                );
    }


    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of(
                    "expenseDate",
                    "amount",
                    "createdAt",
                    "updatedAt",
                    "description"
            );

    private Sort createSort(
            ExpenseFilterRequest filter
    ) {

        if (!ALLOWED_SORT_FIELDS.contains(filter.sortBy())) {
            throw new BusinessException(
                    "Invalid sort field: " + filter.sortBy()
            );
        }

        Sort.Direction direction =
                filter.sortDirection() == SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        return Sort.by(
                direction,
                filter.sortBy()
        );
    }

    private void validateFilter(
            ExpenseFilterRequest filter
    ) {

        if (filter.startDate() != null
                && filter.endDate() != null
                && filter.startDate().isAfter(filter.endDate())) {

            throw new BusinessException(
                    "Start date cannot be after end date"
            );
        }

        if (filter.minAmount() != null
                && filter.maxAmount() != null
                && filter.minAmount()
                .compareTo(filter.maxAmount()) > 0) {

            throw new BusinessException(
                    "Minimum amount cannot be greater than maximum amount"
            );
        }
    }
}
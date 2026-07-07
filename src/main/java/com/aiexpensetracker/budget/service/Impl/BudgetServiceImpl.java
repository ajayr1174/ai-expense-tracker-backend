package com.aiexpensetracker.budget.service.Impl;

import com.aiexpensetracker.budget.dto.request.BudgetCreateRequest;
import com.aiexpensetracker.budget.dto.request.BudgetFilterRequest;
import com.aiexpensetracker.budget.dto.request.BudgetUpdateRequest;
import com.aiexpensetracker.budget.dto.response.BudgetResponse;
import com.aiexpensetracker.budget.entity.Budget;
import com.aiexpensetracker.budget.enums.BudgetPeriodType;
import com.aiexpensetracker.budget.enums.BudgetStatus;
import com.aiexpensetracker.budget.mapper.BudgetMapper;
import com.aiexpensetracker.budget.repository.BudgetRepository;
import com.aiexpensetracker.budget.service.BudgetService;
import com.aiexpensetracker.budget.specification.BudgetSpecification;
import com.aiexpensetracker.category.entity.Category;
import com.aiexpensetracker.category.repository.CategoryRepository;
import com.aiexpensetracker.exception.BusinessException;
import com.aiexpensetracker.exception.ResourceNotFoundException;
import com.aiexpensetracker.expense.repository.ExpenseRepository;
import com.aiexpensetracker.security.service.CurrentUserService;
import com.aiexpensetracker.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import static com.aiexpensetracker.budget.enums.BudgetPeriodType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private static final int DEFAULT_ALERT_THRESHOLD = 80;

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetMapper budgetMapper;

    private final CurrentUserService currentUserService;

    @Override
    public BudgetResponse createBudget(
            BudgetCreateRequest request
    ) {

        User user = currentUserService.getCurrentUser();

        DateRange dateRange = resolveDateRange(
                request.getPeriodType(),
                request.getStartDate(),
                request.getEndDate()
        );

        Category category = resolveCategory(
                request.getCategoryId(),
                user.getId()
        );

        validateOverlap(
                user.getId(),
                category,
                dateRange.startDate(),
                dateRange.endDate()
        );

        Budget budget = budgetMapper.toEntity(request);

        budget.setUser(user);
        budget.setCategory(category);
        budget.setStartDate(dateRange.startDate());
        budget.setEndDate(dateRange.endDate());

        if (budget.getAlertThreshold() == null) {
            budget.setAlertThreshold(DEFAULT_ALERT_THRESHOLD);
        }

        Budget savedBudget =
                budgetRepository.save(budget);

        return buildBudgetResponse(savedBudget);
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetResponse getBudgetById(
            UUID budgetId
    ) {

        User user = currentUserService.getCurrentUser();

        Budget budget = findBudget(
                budgetId,
                user.getId()
        );

        return buildBudgetResponse(budget);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BudgetResponse> getBudgets(
            BudgetFilterRequest filter,
            Pageable pageable
    ) {

        User user = currentUserService.getCurrentUser();

        Specification<Budget> specification =
                BudgetSpecification.build(
                        user.getId(),
                        filter
                );

        return budgetRepository
                .findAll(specification, pageable)
                .map(this::buildBudgetResponse);
    }

    @Override
    public BudgetResponse updateBudget(
            UUID budgetId,
            BudgetUpdateRequest request
    ) {

        User user = currentUserService.getCurrentUser();

        Budget budget = findBudget(
                budgetId,
                user.getId()
        );

        DateRange dateRange = resolveDateRange(
                request.getPeriodType(),
                request.getStartDate(),
                request.getEndDate()
        );

        Category category = resolveCategory(
                request.getCategoryId(),
                user.getId()
        );

        budgetMapper.updateEntity(request, budget);

        budget.setCategory(category);
        budget.setStartDate(dateRange.startDate());
        budget.setEndDate(dateRange.endDate());

        Budget updatedBudget =
                budgetRepository.save(budget);

        return buildBudgetResponse(updatedBudget);
    }

    @Override
    public void deleteBudget(
            UUID budgetId
    ) {

        User user = currentUserService.getCurrentUser();

        Budget budget = findBudget(
                budgetId,
                user.getId()
        );

        budgetRepository.delete(budget);
    }

    private Budget findBudget(
            UUID budgetId,
            UUID userId
    ) {

        return budgetRepository
                .findByIdAndUserId(budgetId, userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Budget not found"
                        )
                );
    }

    private Category resolveCategory(
            UUID categoryId,
            UUID userId
    ) {

        if (categoryId == null) {
            return null;
        }

        return categoryRepository
                .findByIdAndUserId(categoryId, userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Category not found"
                        )
                );
    }

    private BudgetResponse buildBudgetResponse(
            Budget budget
    ) {

        BigDecimal spentAmount =
                calculateSpentAmount(budget);

        BigDecimal remainingAmount =
                budget.getAmount()
                        .subtract(spentAmount)
                        .max(BigDecimal.ZERO);

        BigDecimal usagePercentage =
                calculateUsagePercentage(
                        spentAmount,
                        budget.getAmount()
                );

        BudgetStatus status =
                calculateStatus(
                        usagePercentage,
                        budget.getAlertThreshold()
                );

        BudgetResponse response =
                budgetMapper.toResponse(budget);

        response.setSpentAmount(spentAmount);
        response.setRemainingAmount(remainingAmount);
        response.setUsagePercentage(usagePercentage);
        response.setStatus(status);

        return response;
    }

    private BigDecimal calculateSpentAmount(
            Budget budget
    ) {

        if (budget.getCategory() == null) {

            return expenseRepository
                    .sumAmountByUserAndDateRange(
                            budget.getUser().getId(),
                            budget.getStartDate(),
                            budget.getEndDate()
                    );
        }

        return expenseRepository
                .sumAmountByUserAndCategoryAndDateRange(
                        budget.getUser().getId(),
                        budget.getCategory().getId(),
                        budget.getStartDate(),
                        budget.getEndDate()
                );
    }

    private BigDecimal calculateUsagePercentage(
            BigDecimal spentAmount,
            BigDecimal budgetAmount
    ) {

        return spentAmount
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        budgetAmount,
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private BudgetStatus calculateStatus(
            BigDecimal usagePercentage,
            Integer alertThreshold
    ) {

        if (usagePercentage.compareTo(
                BigDecimal.valueOf(100)
        ) >= 0) {
            return BudgetStatus.EXCEEDED;
        }

        if (usagePercentage.compareTo(
                BigDecimal.valueOf(alertThreshold)
        ) >= 0) {
            return BudgetStatus.WARNING;
        }

        return BudgetStatus.SAFE;
    }

    private void validateOverlap(
            UUID userId,
            Category category,
            LocalDate startDate,
            LocalDate endDate
    ) {

        boolean exists;

        if (category == null) {

            exists =
                    budgetRepository
                            .existsOverlappingOverallBudget(
                                    userId,
                                    startDate,
                                    endDate
                            );

        } else {

            exists =
                    budgetRepository
                            .existsByUserIdAndCategoryIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                    userId,
                                    category.getId(),
                                    endDate,
                                    startDate
                            );
        }

        if (exists) {
            throw new BusinessException(
                    "An overlapping budget already exists"
            );
        }
    }

    private DateRange resolveDateRange(
            BudgetPeriodType periodType,
            LocalDate requestedStartDate,
            LocalDate requestedEndDate
    ) {

        LocalDate referenceDate =
                requestedStartDate != null
                        ? requestedStartDate
                        : LocalDate.now();

        return switch (periodType) {

            case WEEKLY -> {

                LocalDate start =
                        referenceDate.with(
                                TemporalAdjusters.previousOrSame(
                                        DayOfWeek.MONDAY
                                )
                        );

                yield new DateRange(
                        start,
                        start.plusDays(6)
                );
            }

            case MONTHLY -> {

                LocalDate start =
                        referenceDate.withDayOfMonth(1);

                yield new DateRange(
                        start,
                        start.with(
                                TemporalAdjusters.lastDayOfMonth()
                        )
                );
            }

            case YEARLY -> {

                LocalDate start =
                        referenceDate.withDayOfYear(1);

                yield new DateRange(
                        start,
                        start.with(
                                TemporalAdjusters.lastDayOfYear()
                        )
                );
            }

            case CUSTOM -> {

                if (requestedStartDate == null
                        || requestedEndDate == null) {

                    throw new BusinessException(
                            "Start date and end date are required for custom budgets"
                    );
                }

                if (requestedEndDate.isBefore(
                        requestedStartDate
                )) {

                    throw new BusinessException(
                            "End date cannot be before start date"
                    );
                }

                yield new DateRange(
                        requestedStartDate,
                        requestedEndDate
                );
            }
        };
    }

    private record DateRange(
            LocalDate startDate,
            LocalDate endDate
    ) {
    }
}
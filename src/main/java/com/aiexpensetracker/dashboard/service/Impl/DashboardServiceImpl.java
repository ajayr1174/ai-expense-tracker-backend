package com.aiexpensetracker.dashboard.service.Impl;

import com.aiexpensetracker.budget.entity.Budget;
import com.aiexpensetracker.budget.repository.BudgetRepository;
import com.aiexpensetracker.dashboard.dto.response.*;
import com.aiexpensetracker.dashboard.mapper.DashboardMapper;
import com.aiexpensetracker.dashboard.model.DashboardContext;
import com.aiexpensetracker.dashboard.projection.CategorySpendingProjection;
import com.aiexpensetracker.dashboard.projection.MonthlyTrendProjection;
import com.aiexpensetracker.dashboard.service.DashboardService;
import com.aiexpensetracker.exception.BusinessException;
import com.aiexpensetracker.expense.entity.Expense;
import com.aiexpensetracker.expense.repository.ExpenseRepository;
import com.aiexpensetracker.security.service.CurrentUserService;
import com.aiexpensetracker.user.entity.User;
import com.aiexpensetracker.utils.CalculationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final DashboardMapper dashboardMapper;
    private final CurrentUserService currentUserService;

    @Override
    public DashboardResponse getDashboard(
            Integer month,
            Integer year
    ) {

        DashboardContext context =
                buildContext(month, year);

        return new DashboardResponse(

                createPeriod(context),

                getExpenseSummary(context),

                getBudgetSummary(context),

                getCategoryBreakdown(context),

                getMonthlyTrend(context),

                getRecentTransactions(context)
        );
    }

    /**
     * Creates dashboard context.
     */
    private DashboardContext buildContext(
            Integer month,
            Integer year
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        YearMonth period =
                resolveYearMonth(
                        month,
                        year
                );

        return new DashboardContext(

                currentUser,

                period,

                period.atDay(1),

                period.atEndOfMonth()
        );
    }

    /**
     * Dashboard period.
     */
    private DashboardPeriodResponse createPeriod(
            DashboardContext context
    ) {

        return new DashboardPeriodResponse(

                context.period().getMonthValue(),

                context.period().getYear()
        );
    }

    /**
     * Expense Summary
     */
    private ExpenseSummaryResponse getExpenseSummary(
            DashboardContext context
    ) {

        BigDecimal totalExpense =
                expenseRepository.sumTotalAmountByUserId(
                        context.user().getId()
                );

        BigDecimal monthlyExpense =
                expenseRepository.sumAmountByUserAndDateRange(
                        context.user().getId(),
                        context.startDate(),
                        context.endDate()
                );

        BigDecimal todayExpense =
                calculateTodayExpense(context);

        long transactionCount =
                expenseRepository.countByUserIdAndExpenseDateBetween(
                        context.user().getId(),
                        context.startDate(),
                        context.endDate()
                );

        return new ExpenseSummaryResponse(

                totalExpense,

                monthlyExpense,

                todayExpense,

                transactionCount
        );
    }

    private BigDecimal calculateTodayExpense(
            DashboardContext context
    ) {

        LocalDate today = LocalDate.now();

        if (today.isBefore(context.startDate())
                || today.isAfter(context.endDate())) {

            return BigDecimal.ZERO;
        }

        return expenseRepository.sumAmountByUserAndDate(

                context.user().getId(),

                LocalDate.now()
        );
    }

    /**
     * Budget Summary
     */
    private BudgetSummaryResponse getBudgetSummary(
            DashboardContext context
    ) {

        BigDecimal spentAmount =
                expenseRepository.sumAmountByUserAndDateRange(
                        context.user().getId(),
                        context.startDate(),
                        context.endDate()
                );

        Budget budget =
                budgetRepository.findActiveOverallBudget(
                        context.user().getId(),
                        context.startDate()
                ).orElseGet(() ->
                        Budget.builder()
                                .amount(BigDecimal.ZERO)
                                .build()
                );

        BigDecimal totalBudget =
                budget.getAmount();

        BigDecimal remainingAmount =
                totalBudget.subtract(spentAmount);

        BigDecimal usagePercentage =
                CalculationUtils.calculatePercentage(
                        spentAmount,
                        totalBudget
                );

        return new BudgetSummaryResponse(
                totalBudget,
                spentAmount,
                remainingAmount,
                usagePercentage
        );
    }

    /**
     * Category Breakdown
     */
    private List<CategorySpendingResponse> getCategoryBreakdown(
            DashboardContext context
    ) {

        BigDecimal monthlyExpense =
                expenseRepository.sumAmountByUserAndDateRange(
                        context.user().getId(),
                        context.startDate(),
                        context.endDate()
                );

        if (monthlyExpense.compareTo(BigDecimal.ZERO) == 0) {
            return List.of();
        }

        List<CategorySpendingProjection> projections =
                expenseRepository.findCategoryBreakdown(
                        context.user().getId(),
                        context.startDate(),
                        context.endDate()
                );

        return dashboardMapper.toCategorySpendingResponseList(
                projections,
                monthlyExpense
        );
    }

    /**
     * Monthly Trend
     */
    private List<MonthlyTrendResponse> getMonthlyTrend(
            DashboardContext context
    ) {

        LocalDate trendEndDate =
                context.endDate();

        LocalDate trendStartDate =
                trendEndDate.minusMonths(5)
                        .withDayOfMonth(1);

        List<MonthlyTrendProjection> projections =
                expenseRepository.findMonthlyTrend(
                        context.user().getId(),
                        trendStartDate,
                        trendEndDate
                );

        return dashboardMapper.toMonthlyTrendResponseList(
                projections
        );
    }

    /**
     * Recent Transactions
     */
    private List<RecentTransactionResponse> getRecentTransactions(
            DashboardContext context
    ) {

        List<Expense> expenses =
                expenseRepository
                        .findTop5ByUserIdOrderByExpenseDateDescCreatedAtDesc(
                                context.user().getId()
                        );

        return dashboardMapper.toRecentTransactionResponseList(
                expenses
        );
    }

    /**
     * Resolves dashboard period.
     */
    private YearMonth resolveYearMonth(
            Integer month,
            Integer year
    ) {

        if (month == null && year == null) {
            return YearMonth.now();
        }

        if (month == null || year == null) {
            throw new BusinessException(
                    "Both month and year must be provided"
            );
        }

        try {
            return YearMonth.of(
                    year,
                    month
            );
        } catch (Exception exception) {
            throw new BusinessException(
                    "Invalid month or year"
            );
        }
    }

}
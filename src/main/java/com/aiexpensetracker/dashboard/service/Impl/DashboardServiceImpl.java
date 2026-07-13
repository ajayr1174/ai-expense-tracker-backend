package com.aiexpensetracker.dashboard.service.Impl;

import com.aiexpensetracker.dashboard.dto.response.ExpenseSummaryResponse;
import com.aiexpensetracker.dashboard.service.DashboardService;
import com.aiexpensetracker.exception.BusinessException;
import com.aiexpensetracker.expense.repository.ExpenseRepository;
import com.aiexpensetracker.security.service.CurrentUserService;
import com.aiexpensetracker.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl
        implements DashboardService {

    private final ExpenseRepository expenseRepository;
    private final CurrentUserService currentUserService;

    @Override
    public ExpenseSummaryResponse getExpenseSummary(
            Integer month,
            Integer year
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        YearMonth selectedMonth =
                resolveYearMonth(month, year);

        LocalDate startDate =
                selectedMonth.atDay(1);

        LocalDate endDate =
                selectedMonth.atEndOfMonth();

        BigDecimal totalExpense =
                expenseRepository.sumTotalAmountByUserId(
                        currentUser.getId()
                );

        BigDecimal monthlyExpense =
                expenseRepository.sumAmountByUserAndDateRange(
                        currentUser.getId(),
                        startDate,
                        endDate
                );

        BigDecimal todayExpense =
                calculateTodayExpense(
                        currentUser,
                        selectedMonth
                );

        long transactionCount =
                expenseRepository
                        .countByUserIdAndExpenseDateBetween(
                                currentUser.getId(),
                                startDate,
                                endDate
                        );

        return new ExpenseSummaryResponse(
                totalExpense,
                monthlyExpense,
                todayExpense,
                transactionCount
        );
    }

    private BigDecimal calculateTodayExpense(
            User currentUser,
            YearMonth selectedMonth
    ) {

        if (!selectedMonth.equals(YearMonth.now())) {
            return BigDecimal.ZERO;
        }

        return expenseRepository.sumAmountByUserAndDate(
                currentUser.getId(),
                LocalDate.now()
        );
    }

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
            return YearMonth.of(year, month);
        } catch (Exception exception) {
            throw new BusinessException(
                    "Invalid month or year"
            );
        }
    }
}
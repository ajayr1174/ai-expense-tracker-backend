package com.aiexpensetracker.dashboard.mapper;

import com.aiexpensetracker.dashboard.dto.response.MonthlyTrendResponse;
import com.aiexpensetracker.dashboard.dto.response.RecentTransactionResponse;
import com.aiexpensetracker.dashboard.projection.MonthlyTrendProjection;
import com.aiexpensetracker.expense.entity.Expense;
import com.aiexpensetracker.utils.CalculationUtils;
import com.aiexpensetracker.dashboard.dto.response.CategorySpendingResponse;
import com.aiexpensetracker.dashboard.projection.CategorySpendingProjection;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", imports = CalculationUtils.class)
public interface DashboardMapper {

    @Mapping(target = "percentage",
            expression = "java(CalculationUtils.calculatePercentage(projection.getAmount(), monthlyExpense))")
    CategorySpendingResponse toCategorySpendingResponse(
            CategorySpendingProjection projection,
            @Context BigDecimal monthlyExpense
    );

    List<CategorySpendingResponse> toCategorySpendingResponseList(
            List<CategorySpendingProjection> projections,
            @Context BigDecimal monthlyExpense
    );


    @Mapping(target = "expenseId", source = "id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "color", source = "category.color")
    RecentTransactionResponse toRecentTransactionResponse(
            Expense expense
    );

    List<RecentTransactionResponse> toRecentTransactionResponseList(
            List<Expense> expenses
    );


    MonthlyTrendResponse toMonthlyTrendResponse(
            MonthlyTrendProjection projection
    );

    List<MonthlyTrendResponse> toMonthlyTrendResponseList(
            List<MonthlyTrendProjection> projections
    );
}
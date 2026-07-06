package com.aiexpensetracker.expense.mapper;

import com.aiexpensetracker.expense.dto.response.ExpenseResponse;
import com.aiexpensetracker.expense.entity.Expense;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ExpenseMapper {

    @Mapping(
            target = "categoryId",
            source = "category.id"
    )
    @Mapping(
            target = "categoryName",
            source = "category.name"
    )
    ExpenseResponse toResponse(Expense expense);

    
    List<ExpenseResponse> toResponseList(
            List<Expense> expenses
    );
}
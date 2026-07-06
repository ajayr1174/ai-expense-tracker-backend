package com.aiexpensetracker.category.mapper;

import com.aiexpensetracker.category.dto.response.CategoryResponse;
import com.aiexpensetracker.category.entity.Category;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);
}
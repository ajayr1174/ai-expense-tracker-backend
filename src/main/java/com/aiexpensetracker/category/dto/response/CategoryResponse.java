package com.aiexpensetracker.category.dto.response;

import com.aiexpensetracker.category.enums.CategoryType;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(

        UUID id,

        String name,

        String icon,

        String color,

        CategoryType type,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}
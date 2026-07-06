package com.aiexpensetracker.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotBlank(message = "Category name is required")
        @Size(
                max = 100,
                message = "Category name cannot exceed 100 characters"
        )
        String name,

        @Size(
                max = 100,
                message = "Icon cannot exceed 100 characters"
        )
        String icon,

        @Size(
                max = 20,
                message = "Color cannot exceed 20 characters"
        )
        String color

) {
}
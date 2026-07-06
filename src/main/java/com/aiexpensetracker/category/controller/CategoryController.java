package com.aiexpensetracker.category.controller;

import com.aiexpensetracker.category.dto.request.CategoryRequest;
import com.aiexpensetracker.category.dto.response.CategoryResponse;
import com.aiexpensetracker.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>>
    getAllCategories() {

        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse>
    getCategoryById(
            @PathVariable UUID categoryId
    ) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(categoryId)
        );
    }

    @PostMapping
    public ResponseEntity<CategoryResponse>
    createCategory(
            @Valid
            @RequestBody CategoryRequest request
    ) {

        CategoryResponse response =
                categoryService.createCategory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse>
    updateCategory(
            @PathVariable UUID categoryId,
            @Valid
            @RequestBody CategoryRequest request
    ) {

        return ResponseEntity.ok(
                categoryService.updateCategory(
                        categoryId,
                        request
                )
        );
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void>
    deleteCategory(
            @PathVariable UUID categoryId
    ) {

        categoryService.deleteCategory(categoryId);

        return ResponseEntity.noContent().build();
    }
}

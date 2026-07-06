package com.aiexpensetracker.category.service;

import com.aiexpensetracker.category.dto.request.CategoryRequest;
import com.aiexpensetracker.category.dto.response.CategoryResponse;
import com.aiexpensetracker.category.entity.Category;
import com.aiexpensetracker.category.enums.CategoryType;
import com.aiexpensetracker.category.mapper.CategoryMapper;
import com.aiexpensetracker.category.repository.CategoryRepository;
import com.aiexpensetracker.exception.BusinessException;
import com.aiexpensetracker.exception.DuplicateResourceException;
import com.aiexpensetracker.exception.ResourceNotFoundException;
import com.aiexpensetracker.security.service.CurrentUserService;
import com.aiexpensetracker.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CurrentUserService currentUserService;

    public List<CategoryResponse> getAllCategories() {

        User currentUser =
                currentUserService.getCurrentUser();

        return categoryRepository
                .findAccessibleCategories(
                        currentUser.getId(),
                        CategoryType.SYSTEM
                )
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(UUID categoryId) {

        User currentUser =
                currentUserService.getCurrentUser();

        Category category =
                getAccessibleCategory(
                        categoryId,
                        currentUser.getId()
                );

        return categoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponse createCategory(
            CategoryRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        validateDuplicateCategory(
                currentUser.getId(),
                request.name()
        );

        Category category = Category.builder()
                .name(request.name().trim())
                .icon(request.icon())
                .color(request.color())
                .type(CategoryType.CUSTOM)
                .user(currentUser)
                .build();

        Category savedCategory =
                categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse updateCategory(
            UUID categoryId,
            CategoryRequest request
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Category category =
                getOwnedCustomCategory(
                        categoryId,
                        currentUser.getId()
                );

        if (!category.getName()
                .equalsIgnoreCase(request.name().trim())) {

            validateDuplicateCategory(
                    currentUser.getId(),
                    request.name()
            );
        }

        category.setName(request.name().trim());
        category.setIcon(request.icon());
        category.setColor(request.color());

        return categoryMapper.toResponse(category);
    }

    @Transactional
    public void deleteCategory(UUID categoryId) {

        User currentUser =
                currentUserService.getCurrentUser();

        Category category =
                getOwnedCustomCategory(
                        categoryId,
                        currentUser.getId()
                );

        categoryRepository.delete(category);
    }

    private Category getAccessibleCategory(
            UUID categoryId,
            UUID userId
    ) {

        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );

        boolean isSystemCategory =
                category.getType() == CategoryType.SYSTEM;

        boolean isOwnedByUser =
                category.getUser() != null
                        && category.getUser()
                        .getId()
                        .equals(userId);

        if (!isSystemCategory && !isOwnedByUser) {
            throw new ResourceNotFoundException(
                    "Category not found"
            );
        }

        return category;
    }

    private Category getOwnedCustomCategory(
            UUID categoryId,
            UUID userId
    ) {
        Category category =
                getAccessibleCategory(categoryId, userId);

        if (category.getType() == CategoryType.SYSTEM) {
            throw new BusinessException(
                    "System categories cannot be modified"
            );
        }

        return category;
    }

    private void validateDuplicateCategory(
            UUID userId,
            String name
    ) {

        boolean exists =
                categoryRepository
                        .existsByUserIdAndNameIgnoreCase(
                                userId,
                                name.trim()
                        );

        if (exists) {
            throw new DuplicateResourceException(
                    "Category with name '"
                            + name
                            + "' already exists"
            );
        }
    }
}

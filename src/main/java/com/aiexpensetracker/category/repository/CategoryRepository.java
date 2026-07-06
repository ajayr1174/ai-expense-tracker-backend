package com.aiexpensetracker.category.repository;

import com.aiexpensetracker.category.entity.Category;
import com.aiexpensetracker.category.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("""
            SELECT c
            FROM Category c
            WHERE c.type = :systemType
               OR c.user.id = :userId
            ORDER BY c.type DESC, c.name ASC
            """)
    List<Category> findAccessibleCategories(
            @Param("userId") UUID userId,
            @Param("systemType") CategoryType systemType
    );

    boolean existsByUserIdAndNameIgnoreCase(
            UUID userId,
            String name
    );
}
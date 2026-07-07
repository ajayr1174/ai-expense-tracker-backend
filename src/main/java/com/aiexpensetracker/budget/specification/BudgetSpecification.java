package com.aiexpensetracker.budget.specification;

import com.aiexpensetracker.budget.dto.request.BudgetFilterRequest;
import com.aiexpensetracker.budget.entity.Budget;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BudgetSpecification {

    private BudgetSpecification() {
    }

    public static Specification<Budget> build(
            UUID userId,
            BudgetFilterRequest filter
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(
                            root.get("user").get("id"),
                            userId
                    )
            );

            if (filter.getCategoryId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("category").get("id"),
                                filter.getCategoryId()
                        )
                );
            }

            if (filter.getPeriodType() != null) {
                predicates.add(
                        cb.equal(
                                root.get("periodType"),
                                filter.getPeriodType()
                        )
                );
            }

            if (filter.getStartDate() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("startDate"),
                                filter.getStartDate()
                        )
                );
            }

            if (filter.getEndDate() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("endDate"),
                                filter.getEndDate()
                        )
                );
            }

            if (filter.getActive() != null
                    && filter.getActive()) {

                LocalDate today = LocalDate.now();

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("startDate"),
                                today
                        )
                );

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("endDate"),
                                today
                        )
                );
            }

            if (filter.getSearch() != null
                    && !filter.getSearch().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filter.getSearch().toLowerCase() + "%"
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
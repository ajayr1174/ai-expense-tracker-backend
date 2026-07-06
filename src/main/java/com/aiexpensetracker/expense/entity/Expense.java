package com.aiexpensetracker.expense.entity;

import com.aiexpensetracker.category.entity.Category;
import com.aiexpensetracker.expense.enums.ExpenseSource;
import com.aiexpensetracker.expense.enums.PaymentMethod;
import com.aiexpensetracker.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "expenses",
        indexes = {
                @Index(name = "idx_expenses_user_id", columnList = "user_id"),
                @Index(name = "idx_expenses_category_id", columnList = "category_id"),
                @Index(name = "idx_expenses_expense_date", columnList = "expense_date"),
                @Index(
                        name = "idx_expenses_user_date",
                        columnList = "user_id, expense_date"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_expenses_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_expenses_category")
    )
    private Category category;

    @Column(
            name = "amount",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            name = "currency",
            nullable = false,
            length = 3
    )
    private String currency;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false, length = 30)
    private ExpenseSource source;

    @Column(name = "merchant_name", length = 150)
    private String merchantName;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (currency == null) {
            currency = "INR";
        }

        if (source == null) {
            source = ExpenseSource.MANUAL;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
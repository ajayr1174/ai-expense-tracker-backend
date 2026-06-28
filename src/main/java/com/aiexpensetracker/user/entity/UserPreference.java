package com.aiexpensetracker.user.entity;

import com.aiexpensetracker.common.entity.BaseEntity;
import com.aiexpensetracker.user.enums.Theme;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private String currency = "INR";

    @Column(nullable = false)
    @Builder.Default
    private String language = "en";

    @Column(nullable = false)
    @Builder.Default
    private String timezone = "Asia/Kolkata";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Theme theme = Theme.SYSTEM;

    @Column(nullable = false)
    @Builder.Default
    private boolean emailNotification = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean pushNotification = true;
}
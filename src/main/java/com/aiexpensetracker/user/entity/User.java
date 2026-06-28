package com.aiexpensetracker.user.entity;

import com.aiexpensetracker.common.entity.BaseEntity;
import com.aiexpensetracker.user.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(length = 500)
    private String profilePicture;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    private LocalDateTime lastLoginAt;

    private LocalDateTime passwordChangedAt;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ROLE_USER;
    
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private UserPreference preference;
}
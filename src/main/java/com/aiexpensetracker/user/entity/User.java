package com.aiexpensetracker.user.entity;

import com.aiexpensetracker.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    String profilePicture;

    Boolean emailVerified;

    Boolean enabled;
    Boolean accountNonLocked;

    Boolean credentialsNonExpired;

    Boolean accountNonExpired;

    LocalDateTime lastLoginAt;

    LocalDateTime passwordChangedAt;

    Role role;

    Audit Fields;
}
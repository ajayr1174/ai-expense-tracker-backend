package com.aiexpensetracker.user.dto.response;

import com.aiexpensetracker.user.enums.Role;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserProfileResponse {

    private UUID id;

    private String email;

    private String firstName;

    private String lastName;

    private String profilePicture;

    private Role role;

    private boolean emailVerified;

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;
}
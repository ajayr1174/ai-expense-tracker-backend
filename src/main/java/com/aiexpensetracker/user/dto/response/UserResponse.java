package com.aiexpensetracker.user.dto.response;

import com.aiexpensetracker.user.entity.User;
import com.aiexpensetracker.user.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * DTO for {@link User}
 */
@Data
@Builder
public class UserResponse {

    private UUID id;

    private String email;

    private String firstName;

    private String lastName;

    private String profilePicture;

    private Role role;
}
package com.aiexpensetracker.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.aiexpensetracker.user.entity.Users}
 */
@Value
public class UsersDto implements Serializable {
    @Email(message = "Invalid email")
    String email;
    @Size(message = "Name must be minimum 3 characters long")
    String name;
    @Size(min = 8, message = "Password must be minimum 8 characters long")
    String password;
}
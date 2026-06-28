package com.aiexpensetracker.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String firstName;
    private String lastname;

    private String email;

    private String password;
}

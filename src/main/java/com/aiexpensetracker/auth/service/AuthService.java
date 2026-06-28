package com.aiexpensetracker.auth.service;

import com.aiexpensetracker.auth.dto.AuthResponse;
import com.aiexpensetracker.auth.dto.LoginRequest;
import com.aiexpensetracker.auth.dto.SignupRequest;
import com.aiexpensetracker.auth.repository.AuthRepository;
import com.aiexpensetracker.exception.DuplicateResourceException;
import com.aiexpensetracker.exception.InvalidCredentialsException;
import com.aiexpensetracker.exception.ResourceNotFoundException;
import com.aiexpensetracker.security.jwt.JwtService;
import com.aiexpensetracker.user.entity.User;
import com.aiexpensetracker.user.entity.UserPreference;
import com.aiexpensetracker.user.enums.Role;
import com.aiexpensetracker.user.enums.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse signup(SignupRequest request) {

        if (authRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))

                .role(Role.ROLE_USER)

                .enabled(true)
                .emailVerified(false)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)

                .build();
        UserPreference preference = UserPreference.builder()
                .user(user)
                .currency("INR")
                .language("en")
                .timezone("Asia/Kolkata")
                .theme(Theme.SYSTEM)
                .emailNotification(true)
                .pushNotification(true)
                .build();

        user.setPreference(preference);
        authRepository.save(user);

        return AuthResponse.builder()
                .status(true)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (BadCredentialsException ex) {

            throw new InvalidCredentialsException(
                    "Invalid email or password."
            );
        }

        User user = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "email",
                                request.getEmail()));

        String token = jwtService.generateJwtToken(user);

        return AuthResponse.builder()
                .accessToken(token)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastname(user.getLastName())
                .build();

    }
}

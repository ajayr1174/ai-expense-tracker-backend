package com.aiexpensetracker.auth.service;

import com.aiexpensetracker.auth.dto.AuthResponse;
import com.aiexpensetracker.auth.dto.LoginRequest;
import com.aiexpensetracker.auth.dto.SignupRequest;
import com.aiexpensetracker.auth.repository.AuthRepository;
import com.aiexpensetracker.exception.DuplicateResourceException;
import com.aiexpensetracker.security.config.PasswordConfig;
import com.aiexpensetracker.security.jwt.JwtService;
import com.aiexpensetracker.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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
        boolean existingUser = authRepository.existsByEmail(request.getEmail());
        if(existingUser){
            throw new DuplicateResourceException("Email already exist.");
        }
        Users user = Users
                .builder()
                .name(request.getName())
                .email((request.getEmail()))
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        authRepository.save(user);
        return AuthResponse.builder()
                .status(true)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())

        );

        Users user = authRepository.findByEmail(
                        request.getEmail())
                .orElseThrow();

        String token =
                jwtService.generateJwtToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(token)
                .email(user.getEmail())
                .name(user.getName())
                .build();

    }

}

package com.aiexpensetracker.auth.controller;

import com.aiexpensetracker.auth.dto.AuthResponse;
import com.aiexpensetracker.auth.dto.LoginRequest;
import com.aiexpensetracker.auth.dto.SignupRequest;
import com.aiexpensetracker.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @RequestBody SignupRequest request) {

        return ResponseEntity.ok(
                authService.signup(request));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request));

    }
}

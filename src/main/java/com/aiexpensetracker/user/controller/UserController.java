package com.aiexpensetracker.user.controller;

import com.aiexpensetracker.user.dto.request.ChangePasswordRequest;
import com.aiexpensetracker.user.dto.request.UpdateProfileRequest;
import com.aiexpensetracker.user.dto.response.UserProfileResponse;
import com.aiexpensetracker.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {

        return ResponseEntity.ok(
                userService.getCurrentUserProfile()
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserById(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(
                userService.getUserById(userId)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        return ResponseEntity.ok(
                userService.updateProfile(request)
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount() {

        userService.deleteAccount();

        return ResponseEntity.noContent().build();
    }
}
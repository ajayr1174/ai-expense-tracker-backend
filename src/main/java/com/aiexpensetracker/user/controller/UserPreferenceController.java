package com.aiexpensetracker.user.controller;

import com.aiexpensetracker.user.dto.request.UpdatePreferenceRequest;
import com.aiexpensetracker.user.dto.response.UserPreferenceResponse;
import com.aiexpensetracker.user.service.UserPreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/preferences")
@RequiredArgsConstructor
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    @GetMapping
    public ResponseEntity<UserPreferenceResponse> getPreferences() {

        return ResponseEntity.ok(
                userPreferenceService.getPreferences()
        );
    }

    @PutMapping
    public ResponseEntity<UserPreferenceResponse> updatePreferences(
            @Valid @RequestBody UpdatePreferenceRequest request) {

        return ResponseEntity.ok(
                userPreferenceService.updatePreferences(request)
        );
    }
}
package com.aiexpensetracker.user.service;

import com.aiexpensetracker.user.dto.request.ChangePasswordRequest;
import com.aiexpensetracker.user.dto.request.UpdateProfileRequest;
import com.aiexpensetracker.user.dto.response.UserProfileResponse;

import java.util.UUID;

public interface UserService {

    UserProfileResponse getCurrentUserProfile();

    UserProfileResponse getUserById(UUID userId);

    UserProfileResponse updateProfile(UpdateProfileRequest request);

    void changePassword(ChangePasswordRequest request);

    void deleteAccount();
}
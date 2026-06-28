package com.aiexpensetracker.user.service.Impl;

import com.aiexpensetracker.exception.BusinessException;
import com.aiexpensetracker.exception.InvalidCredentialsException;
import com.aiexpensetracker.exception.ResourceNotFoundException;
import com.aiexpensetracker.security.service.CurrentUserService;
import com.aiexpensetracker.user.dto.request.ChangePasswordRequest;
import com.aiexpensetracker.user.dto.request.UpdateProfileRequest;
import com.aiexpensetracker.user.dto.response.UserProfileResponse;
import com.aiexpensetracker.user.entity.User;
import com.aiexpensetracker.user.mapper.UserMapper;
import com.aiexpensetracker.user.repository.UserRepository;
import com.aiexpensetracker.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final CurrentUserService currentUserService;

    @Override
    public UserProfileResponse getCurrentUserProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "email",
                                email));

        return userMapper.toUserProfileResponse(user);
    }

    @Override
    public UserProfileResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "id",
                                userId));

        return userMapper.toUserProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        User user = currentUserService.getCurrentUser();

        userMapper.partialUpdate(request, user);

        User updatedUser = userRepository.save(user);

        return userMapper.toUserProfileResponse(updatedUser);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = currentUserService.getCurrentUser();

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {
            throw new InvalidCredentialsException(
                    "Current password is incorrect.");
        }

        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new BusinessException(
                    "Passwords do not match.");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword()));

        user.setPasswordChangedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public void deleteAccount() {
        User user = currentUserService.getCurrentUser();

        user.setEnabled(false);

        userRepository.save(user);
    }
}

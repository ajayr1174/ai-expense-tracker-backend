package com.aiexpensetracker.user.service.Impl;

import com.aiexpensetracker.exception.ResourceNotFoundException;
import com.aiexpensetracker.security.service.CurrentUserService;
import com.aiexpensetracker.user.dto.request.UpdatePreferenceRequest;
import com.aiexpensetracker.user.dto.response.UserPreferenceResponse;
import com.aiexpensetracker.user.entity.User;
import com.aiexpensetracker.user.entity.UserPreference;
import com.aiexpensetracker.user.mapper.UserPreferenceMapper;
import com.aiexpensetracker.user.repository.UserPreferenceRepository;
import com.aiexpensetracker.user.repository.UserRepository;
import com.aiexpensetracker.user.service.UserPreferenceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
class UserPreferenceImpl implements UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserPreferenceMapper userPreferenceMapper;
    private final CurrentUserService currentUserService;


    @Override
    @Transactional
    public UserPreferenceResponse getPreferences() {

        User user = currentUserService.getCurrentUser();

        UserPreference preference = userPreferenceRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User Preference",
                                "userId",
                                user.getId()));

        return userPreferenceMapper.toResponse(preference);
    }

    @Override
    public UserPreferenceResponse updatePreferences(UpdatePreferenceRequest request) {

        User user = currentUserService.getCurrentUser();

        UserPreference preference = userPreferenceRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User Preference",
                                "userId",
                                user.getId()));

        userPreferenceMapper.updatePreferenceFromRequest(request, preference);

        return userPreferenceMapper.toResponse(preference);
    }

}

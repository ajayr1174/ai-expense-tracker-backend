package com.aiexpensetracker.user.service;

import com.aiexpensetracker.user.dto.request.UpdatePreferenceRequest;
import com.aiexpensetracker.user.dto.response.UserPreferenceResponse;



public interface UserPreferenceService {

    UserPreferenceResponse getPreferences();

    UserPreferenceResponse updatePreferences(UpdatePreferenceRequest request);

}
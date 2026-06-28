package com.aiexpensetracker.user.mapper;

import com.aiexpensetracker.user.dto.request.UpdatePreferenceRequest;
import com.aiexpensetracker.user.dto.response.UserPreferenceResponse;
import com.aiexpensetracker.user.entity.UserPreference;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserPreferenceMapper {

    UserPreferenceResponse toResponse(UserPreference preference);

    void updatePreferenceFromRequest(
            UpdatePreferenceRequest request,
            @MappingTarget UserPreference preference
    );
}

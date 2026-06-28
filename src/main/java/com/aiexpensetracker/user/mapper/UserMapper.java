package com.aiexpensetracker.user.mapper;

import com.aiexpensetracker.user.dto.request.UpdateProfileRequest;
import com.aiexpensetracker.user.dto.response.UserProfileResponse;
import com.aiexpensetracker.user.dto.response.UserResponse;
import com.aiexpensetracker.user.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserResponse userResponse);

    UserResponse toDto(User user);
    UserProfileResponse toUserProfileResponse(User user);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate( UpdateProfileRequest request,
                         @MappingTarget User user);
}
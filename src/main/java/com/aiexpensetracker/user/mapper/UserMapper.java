package com.aiexpensetracker.user.mapper;

import com.aiexpensetracker.user.dto.response.UserResponse;
import com.aiexpensetracker.user.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsersMapper {
    User toEntity(UserResponse userResponse);

    UserResponse toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserResponse userResponse, @MappingTarget User user);
}
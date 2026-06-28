package com.aiexpensetracker.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPreferenceResponse {

    private String currency;

    private String language;

    private String timezone;

    private String theme;

    private boolean emailNotification;

    private boolean pushNotification;
}
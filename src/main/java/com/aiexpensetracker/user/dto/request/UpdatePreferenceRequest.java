package com.aiexpensetracker.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePreferenceRequest {

    @NotBlank
    private String currency;

    @NotBlank
    private String language;

    @NotBlank
    private String timezone;

    @NotBlank
    private String theme;

    private boolean emailNotification;

    private boolean pushNotification;
}
package com.vynqtalk.server.dto.request;

import jakarta.validation.constraints.NotNull;

import com.vynqtalk.server.model.enums.Theme;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsUpdateRequest {
    @NotNull(message = "Notification enabled is required")
    private Boolean notificationEnabled;

    @NotNull(message = "Theme is required")
    private Theme theme;

    @NotNull(message = "Show online status is required")
    private Boolean showOnlineStatus;

}

// Note: Use this DTO for user settings update requests only. 